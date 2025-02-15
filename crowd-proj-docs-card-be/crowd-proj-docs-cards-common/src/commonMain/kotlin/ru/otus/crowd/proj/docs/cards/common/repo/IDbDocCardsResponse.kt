package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError

sealed interface IDbDocCardsResponse: IDbResponse<List<MkPlcDocCard>>

data class DbDocCardsResponseOk(
    val data: List<MkPlcDocCard>
): IDbDocCardsResponse

@Suppress("unused")
data class DbDocCardsResponseError(
    val errors: List<MkPlcDocCardError> = emptyList()
): IDbDocCardsResponse {
    constructor(err: MkPlcDocCardError): this(listOf(err))
}
