package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcOwnerId


data class DbDocCardFilterRequest(
    val titleFilter: String = "",
    val ownerId: MkPlcOwnerId = MkPlcOwnerId.NONE,
    val docCardType: MkPlcDocCardType = MkPlcDocCardType.UNKNOWN,
)
