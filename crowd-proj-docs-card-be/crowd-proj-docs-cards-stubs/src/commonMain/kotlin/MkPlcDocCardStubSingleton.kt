package ru.otus.crowd.proj.docs.cards.be.stubs

import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubPdf.DOC_CARD_PDF1
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubPdf.DOC_CARD_PNG
import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardId
import crowd.proj.docs.cards.common.models.MkPlcDocCardType

object MkPlcDocCardStubSingleton {

    fun get(): MkPlcDocCard = DOC_CARD_PDF1.copy()

    fun prepareResult(block: MkPlcDocCard.() -> Unit): MkPlcDocCard = get().apply(block)

    fun prepareSearchList(filter: String, type: MkPlcDocCardType) = listOf(
        mkPlcDocCardPdf("d-666-01", filter, type),
        mkPlcDocCardPdf("d-666-02", filter, type),
        mkPlcDocCardPdf("d-666-03", filter, type),
        mkPlcDocCardPdf("d-666-04", filter, type),
        mkPlcDocCardPdf("d-666-05", filter, type),
        mkPlcDocCardPdf("d-666-06", filter, type),
    )

    fun prepareOffersList(filter: String, type: MkPlcDocCardType) = listOf(
        mkPlcDocCardPng("s-666-01", filter, type),
        mkPlcDocCardPng("s-666-02", filter, type),
        mkPlcDocCardPng("s-666-03", filter, type),
        mkPlcDocCardPng("s-666-04", filter, type),
        mkPlcDocCardPng("s-666-05", filter, type),
        mkPlcDocCardPng("s-666-06", filter, type),
    )

    private fun mkPlcDocCardPdf(id: String, filter: String, type: MkPlcDocCardType) =
        mkPlcDocCard(DOC_CARD_PDF1, id = id, filter = filter, docType = type)

    private fun mkPlcDocCardPng(id: String, filter: String, type: MkPlcDocCardType) =
        mkPlcDocCard(DOC_CARD_PNG, id = id, filter = filter, docType = type)

    private fun mkPlcDocCard(base: MkPlcDocCard, id: String, filter: String, docType: MkPlcDocCardType) = base.copy(
        id = MkPlcDocCardId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        docCardType = docType
    )
}