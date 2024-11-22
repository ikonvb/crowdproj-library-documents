package ru.otus.otuskotlin.marketplace.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ru.otus.crowd.proj.docs.api.v1.apiV1RequestDeserialize
import ru.otus.crowd.proj.docs.api.v1.apiV1RequestSerialize
import ru.otus.crowd.proj.docs.be.api.v1.models.*

class V1RequestSerializationTest : FunSpec({

    val testMapper = JsonMapper.builder()
        .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
        .build()

    val testRequest = DocCardCreateRequest(

        debug = DocCardDebug(
            mode = DocCardRequestDebugMode.STUB,
            stub = DocCardRequestDebugStubs.SUCCESS
        ),
        requestType = "create",
        docCard = DocCardCreateObject(
            title = "Test doc card title",
            description = "Test doc card description",
            productId = "123",
            ownerId = "321",
            docType = DocType.APPLICATION_SLASH_PDF,
            propertySize = 1024,
            uploadDate = "2024-06-17 15:48:26",
            updateDate = "2024-06-17 15:52:31",
            filePath = "./documents/item_document.pdf",
            visibility = DocCardVisibility.PUBLIC
        )
    )

    context("Serialization Tests") {

        test("should serialize IRequest to JSON") {
            val json = apiV1RequestSerialize(testRequest)
            val expectedJson = testMapper.writeValueAsString(testRequest)

            json shouldBe expectedJson
        }
    }

    context("Deserialization Tests") {

        test("should deserialize JSON to IRequest") {

            val json = apiV1RequestSerialize(testRequest)
            val deserialized = apiV1RequestDeserialize<DocCardCreateRequest>(json)

            deserialized.shouldBeInstanceOf<DocCardCreateRequest>()
            deserialized shouldBe testRequest
        }
    }

    context("Edge Cases") {

        test("should throw error when deserializing invalid JSON for IRequest") {
            val invalidJson = """{ "invalidField": "value" }"""

            shouldThrow<Exception> {
                apiV1RequestDeserialize<DocCardCreateRequest>(invalidJson)
            }
        }

        test("should handle empty JSON gracefully for IRequest") {
            val emptyJson = "{}"

            shouldThrow<Exception> {
                apiV1RequestDeserialize<DocCardCreateRequest>(emptyJson)
            }
        }
    }
})