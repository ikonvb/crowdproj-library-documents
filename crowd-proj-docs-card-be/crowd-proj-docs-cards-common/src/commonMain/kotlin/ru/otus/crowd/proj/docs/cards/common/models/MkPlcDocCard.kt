package ru.otus.crowd.proj.docs.cards.common.models

data class MkPlcDocCard(
    var id: MkPlcDocCardId = MkPlcDocCardId.Companion.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MkPlcOwnerId = MkPlcOwnerId.Companion.NONE,
    var visibility: MkPlcVisibility = MkPlcVisibility.NONE,
    var productId: MkPlcProductId = MkPlcProductId.Companion.NONE,
    var lock: MkPlcDocCardLock = MkPlcDocCardLock.Companion.NONE,
    val permissionsClient: MutableSet<MkPlcDocCardPermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MkPlcDocCard()
    }

}
