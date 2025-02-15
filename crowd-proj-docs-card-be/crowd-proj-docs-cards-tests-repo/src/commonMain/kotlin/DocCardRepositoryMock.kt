package crowd.proj.docs.cards.tests.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.repo.*


class DocCardRepositoryMock(
    private val invokeCreateDocCard: (DbDocCardRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeReadDocCard: (DbDocCardIdRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateDocCard: (DbDocCardRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteDocCard: (DbDocCardIdRequest) -> IDbDocCardResponse = { DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchDocCard: (DbDocCardFilterRequest) -> IDbDocCardsResponse = { DEFAULT_DOC_CARDS_SUCCESS_EMPTY_MOCK },
) : IRepoDocCard {

    override suspend fun createDocCard(rq: DbDocCardRequest): IDbDocCardResponse {
        return invokeCreateDocCard(rq)
    }

    override suspend fun readDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse {
        return invokeReadDocCard(rq)
    }

    override suspend fun updateDocCard(rq: DbDocCardRequest): IDbDocCardResponse {
        return invokeUpdateDocCard(rq)
    }

    override suspend fun deleteDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse {
        return invokeDeleteDocCard(rq)
    }

    override suspend fun searchDocCard(rq: DbDocCardFilterRequest): IDbDocCardsResponse {
        return invokeSearchDocCard(rq)
    }

    companion object {
        val DEFAULT_DOC_CARD_SUCCESS_EMPTY_MOCK = DbDocCardResponseOk(MkPlcDocCard())
        val DEFAULT_DOC_CARDS_SUCCESS_EMPTY_MOCK = DbDocCardsResponseOk(emptyList())
    }
}