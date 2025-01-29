package plugins

import io.ktor.server.application.*
import ru.otus.crowd.proj.logging.common.MkPlcLoggerProvider

expect fun Application.getLoggerProviderConf(): MkPlcLoggerProvider