package crowd.proj.docs.cards.app.common.test

import IMkPlcAppSettings
import controllerHelper
import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.api.v2.mappers.fromTransport
import ru.otus.crowd.proj.docs.api.v2.mappers.toTransportDocCard
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = DocCardCreateRequest(
        docCard = DocCardCreateObject(
            title = "some doc card",
            description = "some description of some doc card",
            docType = DocType.APPLICATION_SLASH_PDF,
            visibility = DocCardVisibility.PUBLIC,
            productId = "some product id",
        ),
        debug = DocCardDebug(mode = DocCardRequestDebugMode.STUB, stub = DocCardRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IMkPlcAppSettings = object : IMkPlcAppSettings {
        override val corSettings: MkPlcDocCardCorSettings = MkPlcDocCardCorSettings()
        override val processor: MkPlcDocCardProcessor = MkPlcDocCardProcessor(corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createDocCardKtor(appSettings: IMkPlcAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<DocCardCreateRequest>()) },
            { toTransportDocCard() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }


    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createDocCardKtor(appSettings) }
        val res = testApp.res as DocCardCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
