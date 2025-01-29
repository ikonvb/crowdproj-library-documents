package ru.otus.crowd.proj.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.crowd.proj.logging.common.MkPlcLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mpLoggerLogback(logger: Logger): MkPlcLogWrapper = MkPlcLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun mpLoggerLogback(clazz: KClass<*>): MkPlcLogWrapper = mpLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun mpLoggerLogback(loggerId: String): MkPlcLogWrapper = mpLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)