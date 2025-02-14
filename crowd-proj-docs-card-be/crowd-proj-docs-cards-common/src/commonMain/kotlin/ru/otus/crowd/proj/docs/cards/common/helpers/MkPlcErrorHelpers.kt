package ru.otus.crowd.proj.docs.cards.common.helpers

import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.logging.common.LogLevel

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

fun MkPlcDocCardContext.addError(error: MkPlcDocCardError) = errors.add(error)

fun MkPlcDocCardContext.fail(error: MkPlcDocCardError) {
    addError(error)
    state = MkPlcDocCardState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MkPlcDocCardError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)