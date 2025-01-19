package v2

import config.MkPlcAppSettings
import io.ktor.server.application.ApplicationCall
import ru.otus.crowd.proj.docs.be.api.v2.models.*
import kotlin.reflect.KClass


val clCreate: KClass<*> = ApplicationCall::createDocCard::class

suspend fun ApplicationCall.createDocCard(appSettings: MkPlcAppSettings) =
    processV2<DocCardCreateRequest, DocCardCreateResponse>(appSettings, clCreate, "create")

val clRead: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.readDocCard(appSettings: MkPlcAppSettings) =
    processV2<DocCardReadRequest, DocCardReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.updateDocCard(appSettings: MkPlcAppSettings) =
    processV2<DocCardUpdateRequest, DocCardUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.deleteDocCard(appSettings: MkPlcAppSettings) =
    processV2<DocCardDeleteRequest, DocCardDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.searchDocCard(appSettings: MkPlcAppSettings) =
    processV2<DocCardSearchRequest, DocCardSearchResponse>(appSettings, clSearch, "search")

val clOffers: KClass<*> = ApplicationCall::createDocCard::class
suspend fun ApplicationCall.offersDocCard(appSettings: MkPlcAppSettings) =
    processV2<DocCardOffersRequest, DocCardOffersResponse>(appSettings, clOffers, "offers")