package crowd.proj.docs.cards.tests.repo

import ru.otus.crowd.proj.docs.cards.common.models.*


abstract class BaseInitDocCards(private val op: String) : IInitObjects<MkPlcDocCard> {
    fun createInitTestModel(
        suf: String,
        ownerId: MkPlcOwnerId = MkPlcOwnerId("owner-123"),
        docCardType: MkPlcDocCardType = MkPlcDocCardType.PDF,
    ) = MkPlcDocCard(
        id = MkPlcDocCardId("docCard-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = MkPlcVisibility.VISIBLE_TO_OWNER,
        docCardType = docCardType,
    )
}
