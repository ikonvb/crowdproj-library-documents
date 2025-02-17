package ru.otus.otuskotlin.marketplace.api.v2


import ru.otus.crowd.proj.docs.api.v2.mappers.fromTransport
import ru.otus.crowd.proj.docs.api.v2.mappers.toTransportDocCard
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class V2MapperTest {
    @Test
    fun fromTransport() {
        val req = DocCardCreateRequest(
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS,
            ),
            docCard = DocCardCreateObject(
                title = "title",
                description = "desc",
                docType = DocType.APPLICATION_SLASH_PDF,
                visibility = DocCardVisibility.PUBLIC,
            ),
        )

        val context = MkPlcDocCardContext()
        context.fromTransport(req)

        assertEquals(MkPlcDocCardStubs.SUCCESS, context.stubCase)
        assertEquals(MkPlcDocCardWorkMode.STUB, context.workMode)
        assertEquals("title", context.mkPlcDocCardRequest.title)
        assertEquals(MkPlcDocCardVisibility.VISIBLE_PUBLIC, context.mkPlcDocCardRequest.visibility)
        assertEquals(MkPlcDocCardType.PDF, context.mkPlcDocCardRequest.docCardType)
    }

    @Test
    fun toTransport() {
        val context = MkPlcDocCardContext(
            requestId = MkPlcDocCardRequestId("1234"),
            command = MkPlcDocCardCommand.CREATE,
            mkPlcDocCardResponse = MkPlcDocCard(
                title = "title",
                description = "desc",
                docCardType = MkPlcDocCardType.PDF,
                visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                MkPlcDocCardError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MkPlcDocCardState.RUNNING,
        )

        val req = context.toTransportDocCard() as DocCardCreateResponse

        assertEquals("title", req.docCard?.title)
        assertEquals("desc", req.docCard?.description)
        assertEquals(DocCardVisibility.PUBLIC, req.docCard?.visibility)
        assertEquals(DocType.IMAGE_SLASH_JPEG, req.docCard?.docType)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
