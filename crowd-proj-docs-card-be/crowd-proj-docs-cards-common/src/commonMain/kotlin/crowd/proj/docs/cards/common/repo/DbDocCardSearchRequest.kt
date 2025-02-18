package crowd.proj.docs.cards.common.repo

import crowd.proj.docs.cards.common.models.MkPlcDocCardType
import crowd.proj.docs.cards.common.models.MkPlcDocCardOwnerId


data class DbDocCardSearchRequest(
    val titleFilter: String = "",
    val ownerId: MkPlcDocCardOwnerId = MkPlcDocCardOwnerId.NONE,
    val docCardType: MkPlcDocCardType = MkPlcDocCardType.UNKNOWN,
)
