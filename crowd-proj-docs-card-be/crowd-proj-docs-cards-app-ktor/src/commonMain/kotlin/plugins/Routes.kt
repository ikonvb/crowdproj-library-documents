package plugins

import config.MkPlcAppSettings
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.crowd.proj.docs.api.v2.apiV2Mapper
import v2.v2DocCard

fun Application.configureRoutes(appSettings: MkPlcAppSettings) {

    routing {

        get("/") {
            call.respondText("Hello, world!")
        }

        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
            v2DocCard(appSettings)
        }
    }
}