package crowd.proj.docs.cards.cassandra.repo

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import crowd.proj.docs.cards.cassandra.repo.model.DocCardCassandraDTO
import crowd.proj.docs.cards.cassandra.repo.model.DocCardVisibility
import crowd.proj.docs.cards.cassandra.repo.model.DocType
import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardId
import crowd.proj.docs.cards.common.models.MkPlcDocCardLock
import crowd.proj.docs.cards.common.repo.*
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RepoDocCardCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<MkPlcDocCard> = emptyList(),
) : DocCardRepoBase(), IRepoDocCard, IDocCardRepoInitializable {

    private val codecRegistry by lazy {

        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(DocCardVisibility::class.java))
            register(EnumNameCodec(DocType::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(DocCardCassandraDTO.table(keyspace, DocCardCassandraDTO.TABLE_NAME))
        session.execute(DocCardCassandraDTO.titleIndex(keyspace, DocCardCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        createSchema(keyspaceName)
        mapper.docCardDao(keyspaceName, DocCardCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(DocCardCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    override fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard> =
        docCards.onEach { dao.create(DocCardCassandraDTO(it)) }

    override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse = tryDocCardMethod {
        val new = rq.docCard.copy(id = MkPlcDocCardId(randomUuid()), lock = MkPlcDocCardLock(randomUuid()))
        dao.create(DocCardCassandraDTO(new)).await()
        DbDocCardResponseOk(new)
    }

    override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse = tryDocCardMethod {
        if (rq.id == MkPlcDocCardId.NONE) return@tryDocCardMethod errorEmptyId
        val res = dao.read(rq.id.asString()).await() ?: return@tryDocCardMethod errorNotFound(rq.id)
        DbDocCardResponseOk(res.toDocCardModel())
    }

    override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse = tryDocCardMethod {
        val idStr = rq.docCard.id.asString()
        val prevLock = rq.docCard.lock.asString()
        val new = rq.docCard.copy(lock = MkPlcDocCardLock(randomUuid()))
        val dto = DocCardCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(DocCardCassandraDTO.COLUMN_LOCK) }
            ?.getString(DocCardCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DbDocCardResponseOk(new)
            resultField == null -> errorNotFound(rq.docCard.id)
            else -> errorRepoConcurrency(
                oldDocCard = dao.read(idStr).await()?.toDocCardModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.docCard.lock
            )
        }
    }

    override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse = tryDocCardMethod {
        val idStr = rq.id.asString()
        val prevLock = rq.lock.asString()
        val oldDocCard = dao.read(idStr).await()?.toDocCardModel() ?: return@tryDocCardMethod errorNotFound(rq.id)
        val res = dao.delete(idStr, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(DocCardCassandraDTO.COLUMN_LOCK) }
            ?.getString(DocCardCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbDocCardResponseOk(oldDocCard)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(idStr).await()?.toDocCardModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse = tryDocCardsMethod {
        val found = dao.search(rq).await()
        DbDocCardsResponseOk(found.map { it.toDocCardModel() })
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}