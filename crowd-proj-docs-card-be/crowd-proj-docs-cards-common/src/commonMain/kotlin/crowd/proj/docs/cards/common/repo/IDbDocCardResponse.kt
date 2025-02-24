package crowd.proj.docs.cards.common.repo

import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardError


sealed interface IDbDocCardResponse : IDbResponse<MkPlcDocCard>

data class DbDocCardResponseOk(
    val data: MkPlcDocCard
) : IDbDocCardResponse

data class DbDocCardResponseError(
    val errors: List<MkPlcDocCardError> = emptyList()
) : IDbDocCardResponse {
    constructor(err: MkPlcDocCardError) : this(listOf(err))
}

data class DbDocCardResponseErrorWithData(
    val data: MkPlcDocCard,
    val errors: List<MkPlcDocCardError> = emptyList()
) : IDbDocCardResponse {
    constructor(docCard: MkPlcDocCard, err: MkPlcDocCardError) : this(docCard, listOf(err))
}
