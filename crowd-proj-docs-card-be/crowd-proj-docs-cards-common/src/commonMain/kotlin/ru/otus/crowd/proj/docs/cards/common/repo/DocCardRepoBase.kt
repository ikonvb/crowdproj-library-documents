package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.helpers.errorSystem

abstract class DocCardRepoBase : IRepoDocCard {

    protected suspend fun tryDocCardMethod(block: suspend () -> IDbDocCardResponse) = try {
        block()
    } catch (e: Throwable) {
        DbDocCardResponseError(errorSystem("methodException", e = e))
    }

    protected suspend fun tryDocCardsMethod(block: suspend () -> IDbDocCardsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbDocCardsResponseError(errorSystem("methodException", e = e))
    }

}
