import config.MkPlcAppSettings
import io.ktor.server.application.*
import plugins.configureCors
import plugins.configureRoutes
import plugins.initAppSettings

fun Application.module(appSettings: MkPlcAppSettings = initAppSettings()) {
    configureCors()
    configureRoutes(appSettings)
}

