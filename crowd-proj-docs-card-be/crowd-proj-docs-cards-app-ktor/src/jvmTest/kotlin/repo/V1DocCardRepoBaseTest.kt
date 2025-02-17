package repo

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
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.toTransportCreate
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.toTransportDelete
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.toTransportRead
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.toTransportUpdate
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardLock
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V1DocCardRepoBaseTest {

    abstract val workMode: DocCardRequestDebugMode
    abstract val appSettingsCreate: MkPlcAppSettings
    abstract val appSettingsRead: MkPlcAppSettings
    abstract val appSettingsUpdate: MkPlcAppSettings
    abstract val appSettingsDelete: MkPlcAppSettings
    abstract val appSettingsSearch: MkPlcAppSettings
    abstract val appSettingsOffers: MkPlcAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val uuidImg = "10000000-0000-0000-0000-000000000003"

    protected val initDocCard: MkPlcDocCard = MkPlcDocCardStubSingleton.prepareResult {
        id = MkPlcDocCardId(uuidOld)
        docCardType = MkPlcDocCardType.PDF
        lock = MkPlcDocCardLock(uuidOld)
    }
    protected val initDocCardImg = MkPlcDocCardStubSingleton.prepareResult {
        id = MkPlcDocCardId(uuidImg)
        docCardType = MkPlcDocCardType.PNG
    }


    @Test
    fun create() {

        val docCard : DocCardCreateObject = initDocCard.toTransportCreate()

        v1TestApplication(
            conf = appSettingsCreate,
            func = "create",
            request = DocCardCreateRequest(
                docCard = docCard,
                debug = DocCardDebug(mode = workMode),
            ),
        ) { response ->

            val responseObj: DocCardCreateResponse = response.body<DocCardCreateResponse>()

            println("responseObj = ${responseObj.docCard}")

            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.docCard?.id)
            assertEquals(docCard.title, responseObj.docCard?.title)
            assertEquals(docCard.description, responseObj.docCard?.description)
            assertEquals(docCard.docType, responseObj.docCard?.docType)
            assertEquals(docCard.visibility, responseObj.docCard?.visibility)
        }
    }

    @Test
    fun read() {

        val docCard = initDocCard.toTransportRead()

        v1TestApplication(
            conf = appSettingsRead,
            func = "read",
            request = DocCardReadRequest(
                docCard = docCard,
                debug = DocCardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as DocCardReadResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.docCard?.id)
        }
    }

    @Test
    fun update() {

        val docCard = initDocCard.toTransportUpdate()

        v1TestApplication(
            conf = appSettingsUpdate,
            func = "update",
            request = DocCardUpdateRequest(
                docCard = docCard,
                debug = DocCardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<DocCardUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(docCard.id, responseObj.docCard?.id)
            assertEquals(docCard.title, responseObj.docCard?.title)
            assertEquals(docCard.description, responseObj.docCard?.description)
            assertEquals(docCard.docType, responseObj.docCard?.docType)
            assertEquals(docCard.visibility, responseObj.docCard?.visibility)
        }
    }

    @Test
    fun delete() {
        val docCard = initDocCard.toTransportDelete()

        v1TestApplication(
            conf = appSettingsDelete,
            func = "delete",
            request = DocCardDeleteRequest(
                docCard = docCard,
                debug = DocCardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<DocCardDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.docCard?.id)
        }
    }

    @Test
    fun search() = v1TestApplication(
        conf = appSettingsSearch,
        func = "search",
        request = DocCardSearchRequest(
            docCardFilter = DocCardSearchFilter(),
            debug = DocCardDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<DocCardSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.docCards?.size)
        assertEquals(uuidOld, responseObj.docCards?.first()?.id)
    }

    @Test
    fun offers() = v1TestApplication(
        conf = appSettingsOffers,
        func = "offers",
        request = DocCardOffersRequest(
            docCardOffers = initDocCard.toTransportRead(),
            debug = DocCardDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<DocCardOffersResponse>()

        println("responseObj = $responseObj")
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.docCards?.size)
        assertEquals(uuidOld, responseObj.docCards?.first()?.id)
    }



    private inline fun <reified T : IRequest> v1TestApplication(
        conf: MkPlcAppSettings,
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {

        application { moduleJvm(appSettings = conf) }

        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response = client.post("/v1/docCard/$func") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
