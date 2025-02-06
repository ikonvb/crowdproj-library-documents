package ru.otus.crowd.proj.docs.cards.be.stubs

import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubPdf.DOC_CARD_DEMAND_PDF1
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubPdf.DOC_CARD_SUPPLY_PDF1
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType

object MkPlcDocCardStubSingleton {

    fun get(): MkPlcDocCard = DOC_CARD_DEMAND_PDF1.copy()

    fun prepareResult(block: MkPlcDocCard.() -> Unit): MkPlcDocCard = get().apply(block)

    fun prepareSearchList(filter: String, type: MkPlcDocCardType) = listOf(
        mkPlcDocCardDemand("d-666-01", filter, type),
        mkPlcDocCardDemand("d-666-02", filter, type),
        mkPlcDocCardDemand("d-666-03", filter, type),
        mkPlcDocCardDemand("d-666-04", filter, type),
        mkPlcDocCardDemand("d-666-05", filter, type),
        mkPlcDocCardDemand("d-666-06", filter, type),
    )

    fun prepareOffersList(filter: String, type: MkPlcDocCardType) = listOf(
        mkPlcDocCardSupply("s-666-01", filter, type),
        mkPlcDocCardSupply("s-666-02", filter, type),
        mkPlcDocCardSupply("s-666-03", filter, type),
        mkPlcDocCardSupply("s-666-04", filter, type),
        mkPlcDocCardSupply("s-666-05", filter, type),
        mkPlcDocCardSupply("s-666-06", filter, type),
    )

    private fun mkPlcDocCardDemand(id: String, filter: String, type: MkPlcDocCardType) =
        mkPlcDocCard(DOC_CARD_DEMAND_PDF1, id = id, filter = filter, docType = type)

    private fun mkPlcDocCardSupply(id: String, filter: String, type: MkPlcDocCardType) =
        mkPlcDocCard(DOC_CARD_SUPPLY_PDF1, id = id, filter = filter, docType = type)

    private fun mkPlcDocCard(base: MkPlcDocCard, id: String, filter: String, docType: MkPlcDocCardType) = base.copy(
        id = MkPlcDocCardId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        docCardType = docType
    )
}