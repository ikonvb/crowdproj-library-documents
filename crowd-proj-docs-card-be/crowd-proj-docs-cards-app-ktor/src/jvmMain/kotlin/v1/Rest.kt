package v1

import config.MkPlcAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1DocCard(appSettings: MkPlcAppSettings) {
    route("docCard") {
        post("create") {
            call.createDocCard(appSettings)
        }
        post("read") {
            call.readDocCard(appSettings)
        }
        post("update") {
            call.updateDocCard(appSettings)
        }
        post("delete") {
            call.deleteDocCard(appSettings)
        }
        post("search") {
            call.searchDocCard(appSettings)
        }
        post("offers") {
            call.offersDocCard(appSettings)
        }
    }
}