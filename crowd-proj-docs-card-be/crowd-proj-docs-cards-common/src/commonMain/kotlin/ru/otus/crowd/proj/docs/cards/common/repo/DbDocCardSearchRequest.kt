package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardOwnerId


data class DbDocCardSearchRequest(
    val titleFilter: String = "",
    val ownerId: MkPlcDocCardOwnerId = MkPlcDocCardOwnerId.NONE,
    val docCardType: MkPlcDocCardType = MkPlcDocCardType.UNKNOWN,
)
