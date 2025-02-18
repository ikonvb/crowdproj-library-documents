package ru.otus.crowd.proj.docs.api.v2.mappers

import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import crowd.proj.docs.cards.common.models.MkPlcDocCardError
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardCreateRequest


private sealed interface Result<T, E>
private data class Ok<T, E>(val value: T) : Result<T, E>
private data class Err<T, E>(val errors: List<E>) : Result<T, E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T, E> Result<T, E>.getOrExec(default: T, block: (Err<T, E>) -> Unit = {}): T = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T, E> Result<T, E>.getOrNull(block: (Err<T, E>) -> Unit = {}): T? = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<MkPlcDocCardStubs, MkPlcDocCardError> = when (this) {
    "success" -> Ok(MkPlcDocCardStubs.SUCCESS)
    "notFound" -> Ok(MkPlcDocCardStubs.NOT_FOUND)
    "badId" -> Ok(MkPlcDocCardStubs.BAD_ID)
    "badTitle" -> Ok(MkPlcDocCardStubs.BAD_TITLE)
    "badDescription" -> Ok(MkPlcDocCardStubs.BAD_DESCRIPTION)
    "badVisibility" -> Ok(MkPlcDocCardStubs.BAD_VISIBILITY)
    "cannotDelete" -> Ok(MkPlcDocCardStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(MkPlcDocCardStubs.BAD_SEARCH_STRING)
    null -> Ok(MkPlcDocCardStubs.NONE)
    else -> Err(
        MkPlcDocCardError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun MkPlcDocCardContext.fromTransportValidated(request: DocCardCreateRequest) {
    command = MkPlcDocCardCommand.CREATE

    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(MkPlcDocCardStubs.NONE) { err: Err<MkPlcDocCardStubs, MkPlcDocCardError> ->
            errors.addAll(err.errors)
            state = MkPlcDocCardState.FAILING
        }
}
