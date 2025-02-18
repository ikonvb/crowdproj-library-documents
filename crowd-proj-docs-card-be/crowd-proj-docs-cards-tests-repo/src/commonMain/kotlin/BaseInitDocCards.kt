package crowd.proj.docs.cards.tests.repo

import crowd.proj.docs.cards.common.models.*


abstract class BaseInitDocCards(private val op: String) : IInitObjects<MkPlcDocCard> {

    open val lockOld: MkPlcDocCardLock = MkPlcDocCardLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MkPlcDocCardLock = MkPlcDocCardLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: MkPlcDocCardOwnerId = MkPlcDocCardOwnerId("owner-123"),
        docCardType: MkPlcDocCardType = MkPlcDocCardType.PDF,
        lock: MkPlcDocCardLock = lockOld,
    ) = MkPlcDocCard(
        id = MkPlcDocCardId("docCard-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = MkPlcDocCardVisibility.VISIBLE_TO_OWNER,
        docCardType = docCardType,
        lock = lock,
    )
}
