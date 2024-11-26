package ru.otus.otuskotlin.marketplace.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ru.otus.crowd.proj.docs.api.v1.apiV1ResponseDeserialize
import ru.otus.crowd.proj.docs.api.v1.apiV1ResponseSerialize
import ru.otus.crowd.proj.docs.be.api.v1.models.*

class V1ResponseSerializationTest : FunSpec({

    val testMapper = JsonMapper.builder()
        .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
        .build()

    val testResponse = DocCardCreateResponse(

        responseType = "create",
        result = ResponseResult.SUCCESS,
        errors = null,
        docCard = DocCardResponseObject(
            id = "111",
            title = "Test doc card title",
            description = "Test doc card description",
            productId = "123",
            ownerId = "321",
            docType = DocType.APPLICATION_SLASH_PDF,
            propertySize = 1024,
            uploadDate = "2024-06-17 15:48:26",
            updateDate = "2024-06-17 15:52:31",
            filePath = "./documents/item_document.pdf",
            visibility = DocCardVisibility.PUBLIC,
            lock = "v1",
            permissions = setOf(DocCardPermissions.READ)
        )
    )

    context("Serialization Tests") {

        test("should serialize IResponse to JSON") {
            val json = apiV1ResponseSerialize(testResponse)
            val expectedJson = testMapper.writeValueAsString(testResponse)
            json shouldBe expectedJson
        }
    }

    context("Deserialization Tests") {

        test("should deserialize JSON to IResponse") {
            val json = apiV1ResponseSerialize(testResponse)
            val deserialized = apiV1ResponseDeserialize<DocCardCreateResponse>(json)
            deserialized.shouldBeInstanceOf<DocCardCreateResponse>()
            deserialized shouldBe testResponse
        }
    }

    context("Edge Cases") {

        test("should throw error when deserializing invalid JSON for IResponse") {
            val invalidJson = """{ "invalidField": "value" }"""

            shouldThrow<Exception> {
                apiV1ResponseDeserialize<DocCardCreateResponse>(invalidJson)
            }
        }

        test("should handle empty JSON gracefully for IResponse") {
            val emptyJson = "{}"

            shouldThrow<Exception> {
                apiV1ResponseDeserialize<DocCardCreateResponse>(emptyJson)
            }
        }
    }
})