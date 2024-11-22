package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.SerializationException
import ru.otus.crowd.proj.docs.api.v2.apiV2ResponseDeserialize
import ru.otus.crowd.proj.docs.api.v2.apiV2ResponseSerialize
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class V2ResponseSerializationTest {


    private val docCardCreateResponse = DocCardCreateResponse(
        docCard = DocCardResponseObject(
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
    fun `test apiV2ResponseSerialize with NullValues`() {
        val requestWithNulls = DocCardCreateResponse(null, null)
        val jsonResult = apiV2ResponseSerialize(requestWithNulls)
        assertEquals("""{"responseType":"create"}""", jsonResult)
    }


    @Test
    fun `test apiV2ResponseDeserialize with InvalidJson`() {
        val invalidJson = """
        {
            "requestType": "create",
            "debug": "invalidDebugData"
        }
    """
        assertFailsWith<SerializationException> {
            apiV2ResponseDeserialize<DocCardCreateResponse>(invalidJson)
        }
    }

    @Test
    fun `test apiV2ResponseDeserialize with EmptyJson`() {
        val emptyJson = "{}"
        assertFailsWith<SerializationException> {
            apiV2ResponseDeserialize<DocCardCreateResponse>(emptyJson)
        }
    }

    @Test
    fun `test apiV2ResponseSerialize Success`() {
        val json = apiV2ResponseSerialize(docCardCreateResponse)
        assertContains(json, Regex("\"title\":\\s*\"Document Title\""))
    }

    @Test
    fun `test apiV2ResponseDeserialize Success`() {
        val json = apiV2ResponseSerialize(docCardCreateResponse)
        val obj = apiV2ResponseDeserialize<IResponse>(json) as DocCardCreateResponse
        assertEquals(docCardCreateResponse, obj)
    }
}
