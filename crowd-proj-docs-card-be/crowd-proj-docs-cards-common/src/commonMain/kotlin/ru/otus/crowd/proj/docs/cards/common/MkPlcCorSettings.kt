package ru.otus.crowd.proj.docs.cards.common

import ru.otus.crowd.proj.logging.common.MkPlcLoggerProvider

data class MkPlcCorSettings(
    val loggerProvider: MkPlcLoggerProvider = MkPlcLoggerProvider(),
) {
    companion object {
        val NONE = MkPlcCorSettings()
    }
}
