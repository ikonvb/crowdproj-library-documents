package crowd.proj.docs.cards.stubs.repo

import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import crowd.proj.docs.cards.common.models.MkPlcDocCardType
import crowd.proj.docs.cards.common.repo.*


class DocCardRepoStub() : IRepoDocCard {

    override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
        return DbDocCardsResponseOk(
            data = MkPlcDocCardStubSingleton.prepareSearchList(filter = "", MkPlcDocCardType.PDF),
        )
    }
}