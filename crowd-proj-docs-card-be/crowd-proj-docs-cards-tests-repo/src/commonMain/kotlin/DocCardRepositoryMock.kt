package crowd.proj.docs.cards.tests.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.repo.*


class DocCardRepositoryMock(
    private val invokeCreateDocCard: (DbDocCardCreateRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeReadDocCard: (DbDocCardReadRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateDocCard: (DbDocCardUpdateRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteDocCard: (DbDocCardDeleteRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchDocCard: (DbDocCardSearchRequest) -> IDbDocCardsResponse = { DEFAULT_DOC_CARDS_SUCCESS_EMPTY_MOCK },
) : IRepoDocCard {


    override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
        return invokeCreateDocCard(rq)
    }

    override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
        return invokeReadDocCard(rq)
    }

    override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
        return invokeUpdateDocCard(rq)
    }

    override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
        return invokeDeleteDocCard(rq)
    }

    override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
        return invokeSearchDocCard(rq)
    }

    companion object {
        val DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK = DbDocCardResponseOk(MkPlcDocCard())
        val DEFAULT_DOC_CARDS_SUCCESS_EMPTY_MOCK = DbDocCardsResponseOk(emptyList())
    }
}