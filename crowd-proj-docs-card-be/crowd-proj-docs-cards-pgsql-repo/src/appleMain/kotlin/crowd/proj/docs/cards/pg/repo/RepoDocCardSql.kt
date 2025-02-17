package crowd.proj.docs.cards.pg.repo

import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import kotlinx.coroutines.runBlocking
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.repo.*


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoDocCardSql actual constructor(
    properties: SqlProperties,
    randomUuid: () -> String
) : IRepoDocCard, IDocCardRepoInitializable {

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }


    actual override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
        TODO("not implemented yet")
    }

    actual override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
        TODO("not implemented yet")
    }

    actual override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
        TODO("not implemented yet")
    }

    actual override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
        TODO("not implemented yet")
    }

    actual override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
        TODO("not implemented yet")
    }

    actual override fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard> {
        return docCards.map {
            runBlocking {
                createDocCard(DbDocCardCreateRequest(it))
            }.let { (it as DbDocCardResponseOk).data }
        }
    }

    actual fun clear() {
        TODO("not implemented yet")
    }

}
