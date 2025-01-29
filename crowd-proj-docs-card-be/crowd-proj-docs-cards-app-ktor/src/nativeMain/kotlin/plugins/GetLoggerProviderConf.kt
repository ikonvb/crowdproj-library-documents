package plugins

import io.ktor.server.application.*
import ru.otus.crowd.proj.logging.common.MkPlcLoggerProvider
import ru.otus.crowd.proj.logging.kermit.mpLoggerKermit

actual fun Application.getLoggerProviderConf(): MkPlcLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp", null -> MkPlcLoggerProvider { mpLoggerKermit(it) }
        else -> throw Exception("Logger $mode is not allowed. Admitted values are kmp and socket")
    }