package stub

import config.MkPlcAppSettings
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
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


class DocCardV1StubApiTest : FunSpec({

    test("should handle create request successfully") {
        v1TestApplication(
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
            response.status shouldBe HttpStatusCode.OK

            val responseObj = response.body<DocCardCreateResponse>()
            responseObj.docCard?.id shouldBe "666"
        }
    }

    test("should handle read request successfully") {
        v1TestApplication(
            func = "read",
            request = DocCardReadRequest(
                docCard = DocCardReadObject(id = "666"),
                debug = DocCardDebug(
                    mode = DocCardRequestDebugMode.STUB,
                    stub = DocCardRequestDebugStubs.SUCCESS
                )
            ),
        ) { response ->
            response.status shouldBe HttpStatusCode.OK

            val responseObj = response.body<DocCardReadResponse>()
            responseObj.docCard?.id shouldBe "666"
        }
    }

    test("should handle update request successfully") {
        v1TestApplication(
            func = "update",
            request = DocCardUpdateRequest(
                docCard = DocCardUpdateObject(
                    id = "666",
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
            response.status shouldBe HttpStatusCode.OK

            val responseObj = response.body<DocCardUpdateResponse>()
            responseObj.docCard?.id shouldBe "666"
        }
    }

    test("should handle delete request successfully") {
        v1TestApplication(
            func = "delete",
            request = DocCardDeleteRequest(
                docCard = DocCardDeleteObject(
                    id = "666",
                ),
                debug = DocCardDebug(
                    mode = DocCardRequestDebugMode.STUB,
                    stub = DocCardRequestDebugStubs.SUCCESS
                )
            ),
        ) { response ->
            response.status shouldBe HttpStatusCode.OK

            val responseObj = response.body<DocCardDeleteResponse>()
            responseObj.docCard?.id shouldBe "666"
        }
    }

    test("should handle search request successfully") {
        v1TestApplication(
            func = "search",
            request = DocCardSearchRequest(
                docCardFilter = DocCardSearchFilter(),
                debug = DocCardDebug(
                    mode = DocCardRequestDebugMode.STUB,
                    stub = DocCardRequestDebugStubs.SUCCESS
                )
            ),
        ) { response ->
            response.status shouldBe HttpStatusCode.OK

            val responseObj = response.body<DocCardSearchResponse>()
            responseObj.docCards?.first()?.id shouldBe "d-666-01"
        }
    }

    test("should handle offers request successfully in v1") {
        v1TestApplication(
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
            response.status shouldBe HttpStatusCode.OK

            val responseObj = response.body<DocCardOffersResponse>()
            responseObj.docCards?.first()?.id shouldBe "d-666-01"
        }
    }

})

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