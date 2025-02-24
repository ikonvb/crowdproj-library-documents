package ru.otus.crowd.proj.docs.cards.api.v1.mappers

import ru.otus.crowd.proj.docs.be.api.v1.models.*
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.exceptions.UnknownRequestError
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs


fun MkPlcDocCardContext.fromTransport(request: IRequest) = when (request) {
    is DocCardCreateRequest -> fromTransport(request)
    is DocCardReadRequest -> fromTransport(request)
    is DocCardUpdateRequest -> fromTransport(request)
    is DocCardDeleteRequest -> fromTransport(request)
    is DocCardSearchRequest -> fromTransport(request)
    is DocCardOffersRequest -> fromTransport(request)
    else -> throw UnknownRequestError(request.javaClass)
}

fun MkPlcDocCardContext.fromTransport(request: DocCardCreateRequest) {
    command = MkPlcDocCardCommand.CREATE
    mkPlcDocCardRequest = request.docCard?.toInternal() ?: MkPlcDocCard()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkPlcDocCardContext.fromTransport(request: DocCardReadRequest) {
    command = MkPlcDocCardCommand.READ
    mkPlcDocCardRequest = request.docCard.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkPlcDocCardContext.fromTransport(request: DocCardUpdateRequest) {
    command = MkPlcDocCardCommand.UPDATE
    mkPlcDocCardRequest = request.docCard?.toInternal() ?: MkPlcDocCard()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkPlcDocCardContext.fromTransport(request: DocCardDeleteRequest) {
    command = MkPlcDocCardCommand.DELETE
    mkPlcDocCardRequest = request.docCard.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkPlcDocCardContext.fromTransport(request: DocCardSearchRequest) {
    command = MkPlcDocCardCommand.SEARCH
    mkPlcDocCardFilterRequest = request.docCardFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkPlcDocCardContext.fromTransport(request: DocCardOffersRequest) {
    command = MkPlcDocCardCommand.OFFERS
    mkPlcDocCardRequest = request.docCardOffers?.id.toDocCardWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DocCardVisibility?.fromTransport(): MkPlcDocCardVisibility = when (this) {
    DocCardVisibility.PUBLIC -> MkPlcDocCardVisibility.VISIBLE_PUBLIC
    DocCardVisibility.OWNER_ONLY -> MkPlcDocCardVisibility.VISIBLE_TO_OWNER
    DocCardVisibility.REGISTERED_ONLY -> MkPlcDocCardVisibility.VISIBLE_TO_GROUP
    null -> MkPlcDocCardVisibility.NONE
}

private fun DocCardReadObject?.toInternal(): MkPlcDocCard = if (this != null) {
    MkPlcDocCard(id = id.toDocCardId())
} else {
    MkPlcDocCard()
}

private fun DocCardDeleteObject?.toInternal(): MkPlcDocCard = if (this != null) {
    MkPlcDocCard(
        id = id.toDocCardId(),
        lock = lock.toDocCardLock(),
    )
} else {
    MkPlcDocCard()
}

private fun DocCardSearchFilter?.toInternal(): MkPlcDocCardFilter = MkPlcDocCardFilter(
    searchString = this?.searchString ?: ""
)

private fun DocCardCreateObject.toInternal(): MkPlcDocCard = MkPlcDocCard(
    title = this.title ?: "",
    description = this.description ?: "",
    docCardType = this.docType.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun DocCardUpdateObject.toInternal(): MkPlcDocCard = MkPlcDocCard(
    id = this.id.toDocCardId(),
    title = this.title ?: "",
    description = this.description ?: "",
    docCardType = this.docType.fromTransport(),
    visibility = this.visibility.fromTransport(),
    lock = lock.toDocCardLock(),
)

private fun String?.toDocCardId() = this?.let { MkPlcDocCardId(it) } ?: MkPlcDocCardId.NONE
private fun String?.toDocCardWithId() = MkPlcDocCard(id = this.toDocCardId())
private fun String?.toDocCardLock() = this?.let { MkPlcDocCardLock(it) } ?: MkPlcDocCardLock.NONE

private fun DocCardDebug?.transportToWorkMode(): MkPlcDocCardWorkMode = when (this?.mode) {
    DocCardRequestDebugMode.PROD -> MkPlcDocCardWorkMode.PROD
    DocCardRequestDebugMode.TEST -> MkPlcDocCardWorkMode.TEST
    DocCardRequestDebugMode.STUB -> MkPlcDocCardWorkMode.STUB
    null -> MkPlcDocCardWorkMode.PROD
}

private fun DocCardDebug?.transportToStubCase(): MkPlcDocCardStubs = when (this?.stub) {
    DocCardRequestDebugStubs.SUCCESS -> MkPlcDocCardStubs.SUCCESS
    DocCardRequestDebugStubs.NOT_FOUND -> MkPlcDocCardStubs.NOT_FOUND
    DocCardRequestDebugStubs.BAD_ID -> MkPlcDocCardStubs.BAD_ID
    DocCardRequestDebugStubs.BAD_TITLE -> MkPlcDocCardStubs.BAD_TITLE
    DocCardRequestDebugStubs.BAD_DESCRIPTION -> MkPlcDocCardStubs.BAD_DESCRIPTION
    DocCardRequestDebugStubs.BAD_VISIBILITY -> MkPlcDocCardStubs.BAD_VISIBILITY
    DocCardRequestDebugStubs.CANNOT_DELETE -> MkPlcDocCardStubs.CANNOT_DELETE
    DocCardRequestDebugStubs.BAD_SEARCH_STRING -> MkPlcDocCardStubs.BAD_SEARCH_STRING
    null -> MkPlcDocCardStubs.NONE
}

private fun DocType?.fromTransport(): MkPlcDocCardType = when (this) {
    DocType.APPLICATION_SLASH_PDF -> MkPlcDocCardType.PDF
    DocType.IMAGE_SLASH_PNG -> MkPlcDocCardType.PNG
    DocType.APPLICATION_SLASH_MSWORD -> MkPlcDocCardType.MS_WORD
    DocType.IMAGE_SLASH_JPEG -> MkPlcDocCardType.JPEG
    null -> MkPlcDocCardType.UNKNOWN
}