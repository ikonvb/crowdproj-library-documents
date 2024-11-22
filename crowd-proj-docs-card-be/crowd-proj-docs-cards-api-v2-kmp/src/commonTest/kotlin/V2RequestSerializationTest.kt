package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.SerializationException
import ru.otus.crowd.proj.docs.api.v2.apiV2RequestDeserialize
import ru.otus.crowd.proj.docs.api.v2.apiV2RequestSerialize
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class V2RequestSerializationTest {

    private val docCardCreateRequest = DocCardCreateRequest(
        debug = DocCardDebug(mode = DocCardRequestDebugMode.STUB, stub = DocCardRequestDebugStubs.SUCCESS),
        docCard = DocCardCreateObject(
            title = "Document Title",
            description = "Document Description",
            productId = "12345",
            ownerId = "67890",
            docType = DocType.APPLICATION_SLASH_PDF,
            propertySize = 1234,
            uploadDate = "2024-11-21T12:00:00Z",
            updateDate = "2024-11-22T12:00:00Z",
            filePath = "/path/to/file",
            visibility = DocCardVisibility.PUBLIC
        )
    )

    @Test
    fun `test apiV2RequestDeserialize Success`() {
        val json = apiV2RequestSerialize(docCardCreateRequest)
        val result: DocCardCreateRequest = apiV2RequestDeserialize(json)
        assertEquals(docCardCreateRequest, result)
    }

    @Test
    fun `test apiV2RequestDeserialize with InvalidJson`() {
        val invalidJson = """
        {
            "requestType": "create",
            "debug": "invalidDebugData"
        }
    """
        assertFailsWith<SerializationException> {
            apiV2RequestDeserialize<DocCardCreateRequest>(invalidJson)
        }
    }

    @Test
    fun `test apiV2RequestDeserialize with MissingFields`() {
        val incompleteJson = """
        {
            "requestType": "create"
        }
    """
        val result: DocCardCreateRequest = apiV2RequestDeserialize(incompleteJson)
        assertEquals(DocCardCreateRequest(null, null), result)
    }


    @Test
    fun `test apiV2RequestSerialize with EmptyObject`() {
        val emptyRequest = DocCardCreateRequest(null, null)
        val jsonResult = apiV2RequestSerialize(emptyRequest)
        assertEquals("""{"requestType":"create"}""", jsonResult)
    }
}
