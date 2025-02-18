package v1

import config.MkPlcAppSettings
import controllerHelper
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.crowd.proj.docs.be.api.v1.models.IRequest
import ru.otus.crowd.proj.docs.be.api.v1.models.IResponse
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.fromTransport
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.toTransportDocCard
import kotlin.reflect.KClass

suspend inline fun <reified T : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: MkPlcAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<T>())
    },
    {
        respond(toTransportDocCard())
    },
    clazz,
    logId,
)
