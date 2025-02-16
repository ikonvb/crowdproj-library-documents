package crowd.proj.docs.cards.pg.repo

import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.repo.*


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoDocCardSql actual constructor(
    properties: SqlProperties,
    randomUuid: () -> String
) : IRepoDocCard, IDocCardRepoInitializable {

    actual override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
        TODO("Not yet implemented")
    }

    actual override fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard> {
        TODO("Not yet implemented")
    }

    actual fun clear() {

    }

}
