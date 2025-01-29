package ru.otus.crowd.proj.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class MkPlcLoggerProvider(private val provider: (String) -> MkPlcLogWrapper = { MkPlcLogWrapper.DEFAULT }) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): MkPlcLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): MkPlcLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): MkPlcLogWrapper = provider(function.name)
}