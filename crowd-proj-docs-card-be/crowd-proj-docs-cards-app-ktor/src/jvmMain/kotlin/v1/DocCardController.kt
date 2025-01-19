package v1

import config.MkPlcAppSettings
import io.ktor.server.application.*
import ru.otus.crowd.proj.docs.be.api.v1.models.*
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.createDocCard(appSettings: MkPlcAppSettings) =
    processV1<DocCardCreateRequest, DocCardCreateResponse>(appSettings, clCreate, "create")

val clRead: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.readDocCard(appSettings: MkPlcAppSettings) =
    processV1<DocCardReadRequest, DocCardReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.updateDocCard(appSettings: MkPlcAppSettings) =
    processV1<DocCardUpdateRequest, DocCardUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.deleteDocCard(appSettings: MkPlcAppSettings) =
    processV1<DocCardDeleteRequest, DocCardDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.searchDocCard(appSettings: MkPlcAppSettings) =
    processV1<DocCardSearchRequest, DocCardSearchResponse>(appSettings, clSearch, "search")

val clOffers: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.offersDocCard(appSettings: MkPlcAppSettings) =
    processV1<DocCardOffersRequest, DocCardOffersResponse>(appSettings, clOffers, "offers")