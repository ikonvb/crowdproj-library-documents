import config.MkPlcAppSettings
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import plugins.initAppSettings
import ru.otus.crowd.proj.docs.api.v1.apiV1Mapper
import v1.v1DocCard


@Suppress("unused")
fun Application.moduleJvm(
    appSettings: MkPlcAppSettings = initAppSettings(),
) {

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }

    module(appSettings)

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            v1DocCard(appSettings)
        }
    }
}