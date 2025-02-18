package crowd.proj.docs.cards.common.models

import ru.otus.crowd.proj.logging.common.LogLevel

data class MkPlcDocCardError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: LogLevel = LogLevel.ERROR
)
