package crowd.proj.docs.cards.pg.repo

import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import crowd.proj.docs.cards.common.helpers.asMkPlcError
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.repo.*


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoDocCardSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoDocCard, IDocCardRepoInitializable {


    private val docCardTable = DocCardTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        docCardTable.deleteAll()
    }

    private fun saveObj(docCard: MkPlcDocCard): MkPlcDocCard = transaction(conn) {
        val res = docCardTable
            .insert {
                to(it, docCard, randomUuid)
            }
            .resultedValues
            ?.map { docCardTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(
        crossinline block: () -> T,
        crossinline handle: (Exception) -> T
    ): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbDocCardResponse): IDbDocCardResponse =
        transactionWrapper(block) { DbDocCardResponseError(it.asMkPlcError()) }

    actual override fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard> =
        docCards.map { saveObj(it) }

    actual override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse = transactionWrapper {
        DbDocCardResponseOk(saveObj(rq.docCard))
    }

    private fun read(id: MkPlcDocCardId): IDbDocCardResponse {
        val res = docCardTable.selectAll().where {
            docCardTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbDocCardResponseOk(docCardTable.from(res))
    }

    actual override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse =
        transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: MkPlcDocCardId,
        lock: MkPlcDocCardLock,
        block: (MkPlcDocCard) -> IDbDocCardResponse
    ): IDbDocCardResponse =
        transactionWrapper {
            if (id == MkPlcDocCardId.NONE) return@transactionWrapper errorEmptyId

            val current = docCardTable.selectAll().where { docCardTable.id eq id.asString() }
                .singleOrNull()
                ?.let { docCardTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    actual override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse =
        update(rq.docCard.id, rq.docCard.lock) {
            docCardTable.update({ docCardTable.id eq rq.docCard.id.asString() }) {
                to(it, rq.docCard.copy(lock = MkPlcDocCardLock(randomUuid())), randomUuid)
            }
            read(rq.docCard.id)
        }

    actual override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse = update(rq.id, rq.lock) {
        docCardTable.deleteWhere { id eq rq.id.asString() }
        DbDocCardResponseOk(it)
    }

    actual override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse =
        transactionWrapper({
            val res = docCardTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != MkPlcDocCardOwnerId.NONE) {
                        add(docCardTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.docCardType != MkPlcDocCardType.UNKNOWN) {
                        add(docCardTable.docType eq rq.docCardType)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (docCardTable.title like "%${rq.titleFilter}%")
                                    or (docCardTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbDocCardsResponseOk(data = res.map { docCardTable.from(it) })
        }, {
            DbDocCardsResponseError(it.asMkPlcError())
        })
}
