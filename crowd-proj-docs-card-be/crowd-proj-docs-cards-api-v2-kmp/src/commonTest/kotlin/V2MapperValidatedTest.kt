package ru.otus.otuskotlin.marketplace.api.v2

import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.docs.api.v2.mappers.fromTransportValidated
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardCreateRequest
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardDebug
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class V2MapperValidatedTest {
    @Test
    fun fromTransportValidated() {
        val req = DocCardCreateRequest(
            debug = DocCardDebug(
                stub = DocCardRequestDebugStubs.SUCCESS,
            ),
        )

        val context = MkPlcDocCardContext()
        context.fromTransportValidated(req)

        assertEquals(MkPlcDocCardStubs.SUCCESS, context.stubCase)
    }
}
