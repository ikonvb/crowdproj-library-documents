package stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import config.MkPlcAppSettings
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import moduleJvm
import ru.otus.crowd.proj.docs.be.api.v1.models.*
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals


class DocCardV1StubApiTest {

    @Test
    fun `should handle create request successfully`() = v1TestApplication(
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
        assertEquals(HttpStatusCode.OK, response.status)
        val responseObj = response.body<DocCardCreateResponse>()
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun `should handle read request successfully`() = v1TestApplication(
        func = "read",
        request = DocCardReadRequest(
            docCard = DocCardReadObject(id = "111"),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        assertEquals(HttpStatusCode.OK, response.status)
        val responseObj = response.body<DocCardReadResponse>()
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun `should handle update request successfully`() = v1TestApplication(
        func = "update",
        request = DocCardUpdateRequest(
            docCard = DocCardUpdateObject(
                id = "111",
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
        assertEquals(HttpStatusCode.OK, response.status)
        val responseObj = response.body<DocCardUpdateResponse>()
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun `should handle delete request successfully`() = v1TestApplication(
        func = "delete",
        request = DocCardDeleteRequest(
            docCard = DocCardDeleteObject(
                id = "111",
            ),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        assertEquals(HttpStatusCode.OK, response.status)
        val responseObj = response.body<DocCardDeleteResponse>()
        assertEquals("111", responseObj.docCard?.id)
    }

    @Test
    fun `should handle search request successfully`() = v1TestApplication(
        func = "search",
        request = DocCardSearchRequest(
            docCardFilter = DocCardSearchFilter(),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        assertEquals(HttpStatusCode.OK, response.status)
        val responseObj = response.body<DocCardSearchResponse>()
        assertEquals("d-666-01", responseObj.docCards?.first()?.id)
    }

    @Test
    fun `should handle offers request successfully in v1`() = v1TestApplication(
        func = "offers",
        request = DocCardOffersRequest(
            docCardOffers = DocCardReadObject(
                id = "666",
            ),
            debug = DocCardDebug(
                mode = DocCardRequestDebugMode.STUB,
                stub = DocCardRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        assertEquals(HttpStatusCode.OK, response.status)
        val responseObj = response.body<DocCardOffersResponse>()
        assertEquals("d-666-01", responseObj.docCards?.first()?.id)
    }
}

private fun v1TestApplication(
    func: String,
    request: IRequest,
    function: suspend (HttpResponse) -> Unit,
): Unit = testApplication {
    application { moduleJvm(MkPlcAppSettings(corSettings = MkPlcCorSettings())) }
    val client = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
    val response = client.post("/v1/docCard/$func") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }
    function(response)
}