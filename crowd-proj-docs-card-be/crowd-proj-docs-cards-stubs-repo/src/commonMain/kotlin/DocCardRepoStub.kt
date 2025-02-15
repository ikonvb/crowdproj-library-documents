package crowd.proj.docs.cards.stubs.repo

import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.repo.*


class DocCardRepoStub() : IRepoDocCard {

    override suspend fun createDocCard(rq: DbDocCardRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun readDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun updateDocCard(rq: DbDocCardRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun deleteDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse {
        return DbDocCardResponseOk(
            data = MkPlcDocCardStubSingleton.get(),
        )
    }

    override suspend fun searchDocCard(rq: DbDocCardFilterRequest): IDbDocCardsResponse {
        return DbDocCardsResponseOk(
            data = MkPlcDocCardStubSingleton.prepareSearchList(filter = "", MkPlcDocCardType.PDF),
        )
    }
}