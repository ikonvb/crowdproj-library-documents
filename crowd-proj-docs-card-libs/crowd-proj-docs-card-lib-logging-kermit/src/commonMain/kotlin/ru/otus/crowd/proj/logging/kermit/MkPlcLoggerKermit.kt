package ru.otus.crowd.proj.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import ru.otus.crowd.proj.logging.common.MkPlcLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun mpLoggerKermit(loggerId: String): MkPlcLogWrapper {

    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return MkPlcLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun mpLoggerKermit(cls: KClass<*>): MkPlcLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return MkPlcLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName ?: "",
    )
}