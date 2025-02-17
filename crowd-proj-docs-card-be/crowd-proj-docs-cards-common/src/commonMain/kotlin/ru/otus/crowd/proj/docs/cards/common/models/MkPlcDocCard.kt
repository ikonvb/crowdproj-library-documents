package ru.otus.crowd.proj.docs.cards.common.models

data class MkPlcDocCard(
    var id: MkPlcDocCardId = MkPlcDocCardId.Companion.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MkPlcDocCardOwnerId = MkPlcDocCardOwnerId.Companion.NONE,
    var docCardType: MkPlcDocCardType = MkPlcDocCardType.UNKNOWN,
    var visibility: MkPlcDocCardVisibility = MkPlcDocCardVisibility.NONE,
    var productId: MkPlcDocCardProductId = MkPlcDocCardProductId.Companion.NONE,
    var lock: MkPlcDocCardLock = MkPlcDocCardLock.Companion.NONE,
    val permissionsClient: MutableSet<MkPlcDocCardPermissionClient> = mutableSetOf()
) {

    fun deepCopy(): MkPlcDocCard = copy(permissionsClient = permissionsClient.toMutableSet())

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MkPlcDocCard()
    }

}
