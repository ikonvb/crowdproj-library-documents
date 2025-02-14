package stub

import config.MkPlcAppSettings
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import module
import ru.otus.crowd.proj.docs.api.v2.apiV2Mapper
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals


class DocCardV2StubApiTest {

    @Test
    fun create() = v2TestApplication(
        func = "create",

        request = DocCardCreateRequest(
            docCard = DocCardCreateObject(
                title = "Документ №33",
                description = "Документ о товаре",
                docType = DocType.APPLICATION_SLASH_PDF,
                visibility = DocCardVisibility.PUBLIC,
            ),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DocCardCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun read() = v2TestApplication(
        func = "read",
        request = DocCardReadRequest(
            docCard = DocCardReadObject("111"),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DocCardReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun update() = v2TestApplication(
        func = "update",
        request = DocCardUpdateRequest(
            docCard = DocCardUpdateObject(
                id = "111",
                title = "Документ о товаре",
                description = "Документ о товаре",
                docType = DocType.APPLICATION_SLASH_PDF,
                visibility = DocCardVisibility.PUBLIC,
            ),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DocCardUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun delete() = v2TestApplication(
        func = "delete",
        request = DocCardDeleteRequest(
            docCard = DocCardDeleteObject(
                id = "111",
                lock = "123"
            ),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DocCardDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun search() = v2TestApplication(
        func = "search",
        request = DocCardSearchRequest(
            docCardFilter = DocCardSearchFilter(),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DocCardSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.docCards?.first()?.id)
    }

    @Test
    fun offers() = v2TestApplication(
        func = "offers",
        request = DocCardOffersRequest(
            docCardOffers = DocCardReadObject(
                id = "111"
            ),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DocCardOffersResponse>()
        assertEquals(200, response.status.value)
        assertEquals("s-666-01", responseObj.docCards?.first()?.id)
    }

    private inline fun <reified T : IRequest> v2TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(MkPlcAppSettings(corSettings = MkPlcCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/docCard/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}