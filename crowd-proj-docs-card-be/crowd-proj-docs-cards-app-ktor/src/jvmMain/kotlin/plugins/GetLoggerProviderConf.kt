package plugins

import io.ktor.server.application.*
import ru.otus.crowd.proj.logging.common.MkPlcLoggerProvider
import ru.otus.crowd.proj.logging.kermit.mpLoggerKermit
import ru.otus.crowd.proj.logging.logback.mpLoggerLogback

actual fun Application.getLoggerProviderConf(): MkPlcLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> MkPlcLoggerProvider { mpLoggerKermit(it) }
        "logback", null -> MkPlcLoggerProvider { mpLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Admitted values are kmp, socket and logback (default)")
    }