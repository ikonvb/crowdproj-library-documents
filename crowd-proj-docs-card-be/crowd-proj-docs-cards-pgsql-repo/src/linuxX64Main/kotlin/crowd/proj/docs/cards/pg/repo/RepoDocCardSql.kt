package crowd.proj.docs.cards.pg.repo

import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import crowd.proj.docs.cards.pg.repo.SqlFields.quoted
import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.repo.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoDocCardSql actual constructor(
    properties: SqlProperties,
    val randomUuid: () -> String,
) : IRepoDocCard, IDocCardRepoInitializable {

    init {
        require(properties.database.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.schema.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL schema must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.table.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL table must contain only letters, numbers and underscore symbol '_'"
        }
    }


    private val dbName: String = "\"${properties.schema}\".\"${properties.table}\"".apply {
        "PostgreSQL table name must contain only letters, numbers and underscore symbol '_'"
    }

    init {
        initConnection(properties)
    }

    private suspend fun saveElement(saveDocCard: MkPlcDocCard): IDbDocCardResponse {
        val sql = """
                INSERT INTO $dbName (
                  ${SqlFields.ID.quoted()}, 
                  ${SqlFields.TITLE.quoted()}, 
                  ${SqlFields.DESCRIPTION.quoted()},
                  ${SqlFields.VISIBILITY.quoted()},
                  ${SqlFields.DOC_TYPE.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()},
                  ${SqlFields.PRODUCT_ID.quoted()}
                ) VALUES (
                  :${SqlFields.ID}, 
                  :${SqlFields.TITLE}, 
                  :${SqlFields.DESCRIPTION}, 
                  :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE}, 
                  :${SqlFields.DOC_TYPE}::${SqlFields.DOC_TYPE_TYPE}, 
                  :${SqlFields.LOCK}, 
                  :${SqlFields.OWNER_ID}, 
                  :${SqlFields.PRODUCT_ID}
                )
                RETURNING ${SqlFields.allFields.joinToString()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            saveDocCard.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbDocCardResponseOk(res.first())
    }

    actual override fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard> = runBlocking {
        docCards.map {
            val res = saveElement(it)
            if (res !is DbDocCardResponseOk) throw Exception()
            res.data
        }
    }

    actual override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
        val saveDocCard = rq.docCard.copy(id = MkPlcDocCardId(randomUuid()), lock = MkPlcDocCardLock(randomUuid()))
        return saveElement(saveDocCard)
    }

    actual override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return if (res.isEmpty()) errorNotFound(rq.id) else DbDocCardResponseOk(res.first())
    }

    actual override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
        val sql = """
            WITH update_obj AS (
                UPDATE $dbName a
                SET ${SqlFields.TITLE.quoted()} = :${SqlFields.TITLE}
                , ${SqlFields.DESCRIPTION.quoted()} = :${SqlFields.DESCRIPTION}
                , ${SqlFields.DOC_TYPE.quoted()} = :${SqlFields.DOC_TYPE}::${SqlFields.DOC_TYPE_TYPE}
                , ${SqlFields.VISIBILITY.quoted()} = :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE}
                , ${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK}
                , ${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}
                , ${SqlFields.PRODUCT_ID.quoted()} = :${SqlFields.PRODUCT_ID}
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING ${SqlFields.allFields.joinToString()}
            ),
            select_obj AS (
                SELECT ${SqlFields.allFields.joinToString()} FROM $dbName 
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
            )
            (SELECT * FROM update_obj UNION ALL SELECT * FROM select_obj) LIMIT 1
        """.trimIndent()
        val rqDocCard = rq.docCard
        val newDocCard = rqDocCard.copy(lock = MkPlcDocCardLock(randomUuid()))
        val res = driver.execute(
            sql = sql,
            newDocCard.toDb() + rqDocCard.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        val returnedDocCard: MkPlcDocCard? = res.firstOrNull()
        return when {
            returnedDocCard == null -> errorNotFound(rq.docCard.id)
            returnedDocCard.lock == newDocCard.lock -> DbDocCardResponseOk(returnedDocCard)
            else -> errorRepoConcurrency(returnedDocCard, rqDocCard.lock)
        }
    }

    actual override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
        val sql = """
            WITH delete_obj AS (
                DELETE FROM $dbName a
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING '${SqlFields.DELETE_OK}'
            )
            SELECT ${SqlFields.allFields.joinToString()}, (SELECT * FROM delete_obj) as flag FROM $dbName 
            WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
        """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb() + rq.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) to row.getString(SqlFields.allFields.size) }
        val returnedPair: Pair<MkPlcDocCard, String?>? = res.firstOrNull()
        val returnedDocCard: MkPlcDocCard? = returnedPair?.first
        return when {
            returnedDocCard == null -> errorNotFound(rq.id)
            returnedPair.second == SqlFields.DELETE_OK -> DbDocCardResponseOk(returnedDocCard)
            else -> errorRepoConcurrency(returnedDocCard, rq.lock)
        }
    }

    actual override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
        val where = listOfNotNull(
            rq.ownerId.takeIf { it != MkPlcDocCardOwnerId.NONE }
                ?.let { "${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}" },
            rq.docCardType.takeIf { it != MkPlcDocCardType.UNKNOWN }
                ?.let { "${SqlFields.DOC_TYPE.quoted()} = :${SqlFields.DOC_TYPE}::${SqlFields.DOC_TYPE_TYPE}" },
            rq.titleFilter.takeIf { it.isNotBlank() }
                ?.let { "${SqlFields.TITLE.quoted()} LIKE :${SqlFields.TITLE}" },
        )
            .takeIf { it.isNotEmpty() }
            ?.let { "WHERE ${it.joinToString(separator = " AND ")}" }
            ?: ""

        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName $where
            """.trimIndent()
        println("SQL: $sql")
        val res = driver.execute(
            sql = sql,
            rq.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbDocCardsResponseOk(res)
    }

    actual fun clear(): Unit = runBlocking {
        val sql = """
                DELETE FROM $dbName 
            """.trimIndent()
        driver.execute(sql = sql)
    }

    companion object {
        private lateinit var driver: PostgresDriver
        private fun initConnection(properties: SqlProperties) {
            if (!this::driver.isInitialized) {
                driver = PostgresDriver(
                    host = properties.host,
                    port = properties.port,
                    user = properties.user,
                    database = properties.database,
                    password = properties.password,
                )
            }
        }
    }
}
