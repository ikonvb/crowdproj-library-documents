package ru.otus.crowd.proj.docs.cards.be.stubs

import ru.otus.crowd.proj.docs.cards.common.models.*

object MkPlcDocCardStubPdf {

    val DOC_CARD_DEMAND_PDF1: MkPlcDocCard
        get() = MkPlcDocCard(
            id = MkPlcDocCardId("111"),
            title = "Документ №33",
            description = "Документ об очень важном",
            ownerId = MkPlcDocCardOwnerId("user-1"),
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                MkPlcDocCardPermissionClient.READ,
                MkPlcDocCardPermissionClient.UPDATE,
                MkPlcDocCardPermissionClient.DELETE,
                MkPlcDocCardPermissionClient.MAKE_VISIBLE_PUBLIC,
                MkPlcDocCardPermissionClient.MAKE_VISIBLE_GROUP,
                MkPlcDocCardPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )

    val DOC_CARD_SUPPLY_PDF1 = DOC_CARD_DEMAND_PDF1.copy()

}