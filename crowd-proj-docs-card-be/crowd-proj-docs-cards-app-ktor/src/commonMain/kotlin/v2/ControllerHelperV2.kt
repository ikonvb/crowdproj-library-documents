package v2

import config.MkPlcAppSettings
import controllerHelper
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.crowd.proj.docs.api.v2.mappers.fromTransport
import ru.otus.crowd.proj.docs.api.v2.mappers.toTransportDocCard
import ru.otus.crowd.proj.docs.be.api.v2.models.IRequest
import ru.otus.crowd.proj.docs.be.api.v2.models.IResponse
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: MkPlcAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(this@processV2.receive<Q>())
    },
    { this@processV2.respond(toTransportDocCard() as R) },
    clazz,
    logId,
)