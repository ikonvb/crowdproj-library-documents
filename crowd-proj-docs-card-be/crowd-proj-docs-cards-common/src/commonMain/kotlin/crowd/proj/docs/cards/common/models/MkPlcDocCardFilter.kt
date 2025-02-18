package crowd.proj.docs.cards.common.models

data class MkPlcDocCardFilter(
    var searchString: String = "",
    var ownerId: MkPlcDocCardOwnerId = MkPlcDocCardOwnerId.Companion.NONE,
    var docCardType: MkPlcDocCardType = MkPlcDocCardType.UNKNOWN
) {
    fun deepCopy(): MkPlcDocCardFilter = copy()
}
