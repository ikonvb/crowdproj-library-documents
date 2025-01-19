package ru.otus.crowd.proj.docs.cards.common.helpers

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError

fun Throwable.asMkPlcError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MkPlcDocCardError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)