package ru.otus.crowd.proj.docs.cards.common.models

data class MkPlcDocCardFilter(
    var searchString: String = "",
    var ownerId: MkPlcOwnerId = MkPlcOwnerId.Companion.NONE
)
