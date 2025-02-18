package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.crowd.proj.docs.api.v2.mappers.fromTransport
import ru.otus.crowd.proj.docs.api.v2.mappers.toTransportDocCard
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import  crowd.proj.docs.cards.common.MkPlcDocCardContext
import  crowd.proj.docs.cards.common.models.*
import  crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class V2MapperDeleteTest {
    @Test
    fun fromTransport() {
        val req = DocCardDeleteRequest(
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS,
            ),
            docCard = DocCardDeleteObject(
                id = "12345",
                lock = "456789",
            ),
        )

        val context = MkPlcDocCardContext()
        context.fromTransport(req)

        assertEquals(MkPlcDocCardStubs.SUCCESS, context.stubCase)
        assertEquals(MkPlcDocCardWorkMode.STUB, context.workMode)
        assertEquals("12345", context.mkPlcDocCardRequest.id.asString())
        assertEquals("456789", context.mkPlcDocCardRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = MkPlcDocCardContext(
            requestId = MkPlcDocCardRequestId("1234"),
            command = MkPlcDocCardCommand.DELETE,
            mkPlcDocCardResponse = MkPlcDocCard(
                id = MkPlcDocCardId("12345"),
                title = "title",
                description = "desc",
                docCardType = MkPlcDocCardType.PDF,
                visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
                lock = MkPlcDocCardLock("456789"),
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

        val req = context.toTransportDocCard() as DocCardDeleteResponse

        assertEquals("12345", req.docCard?.id)
        assertEquals("456789", req.docCard?.lock)
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
