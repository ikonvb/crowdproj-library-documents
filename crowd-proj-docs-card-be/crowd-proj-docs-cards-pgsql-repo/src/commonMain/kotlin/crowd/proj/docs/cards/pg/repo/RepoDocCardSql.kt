package crowd.proj.docs.cards.pg.repo

import com.benasher44.uuid.uuid4
import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.repo.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoDocCardSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoDocCard, IDocCardRepoInitializable {
    override fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard>
    override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse
    override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse
    override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse
    override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse
    override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse
    fun clear()
}