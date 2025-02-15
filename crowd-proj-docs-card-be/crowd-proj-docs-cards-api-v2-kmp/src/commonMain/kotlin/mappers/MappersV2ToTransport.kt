package ru.otus.crowd.proj.docs.api.v2.mappers

import ru.otus.crowd.proj.docs.be.api.v2.models.*
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.exceptions.UnknownMkPlcCommand
import ru.otus.crowd.proj.docs.cards.common.models.*

fun MkPlcDocCardContext.toTransportDocCard(): IResponse = when (val cmd = command) {
    MkPlcDocCardCommand.CREATE -> toTransportCreate()
    MkPlcDocCardCommand.READ -> toTransportRead()
    MkPlcDocCardCommand.UPDATE -> toTransportUpdate()
    MkPlcDocCardCommand.DELETE -> toTransportDelete()
    MkPlcDocCardCommand.SEARCH -> toTransportSearch()
    MkPlcDocCardCommand.OFFERS -> toTransportOffers()
    MkPlcDocCardCommand.INIT -> toTransportInit()
    MkPlcDocCardCommand.FINISH -> throw UnknownMkPlcCommand(cmd)
    MkPlcDocCardCommand.NONE -> throw UnknownMkPlcCommand(cmd)
}

fun MkPlcDocCardContext.toTransportCreate() = DocCardCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    docCard = mkPlcDocCardResponse.toTransportDocCard()
)

fun MkPlcDocCardContext.toTransportRead() = DocCardReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    docCard = mkPlcDocCardResponse.toTransportDocCard()
)

fun MkPlcDocCardContext.toTransportUpdate() = DocCardUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    docCard = mkPlcDocCardResponse.toTransportDocCard()
)

fun MkPlcDocCardContext.toTransportDelete() = DocCardDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    docCard = mkPlcDocCardResponse.toTransportDocCard()
)

fun MkPlcDocCardContext.toTransportSearch() = DocCardSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    docCards = mkPlcDocCardsResponse.toTransportDocCard()
)

fun MkPlcDocCardContext.toTransportOffers() = DocCardOffersResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    docCards = mkPlcDocCardsResponse.toTransportDocCard()
)

fun MkPlcDocCardContext.toTransportInit() = DocCardInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<MkPlcDocCard>.toTransportDocCard(): List<DocCardResponseObject>? = this
    .map { it.toTransportDocCard() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkPlcDocCard.toTransportDocCard(): DocCardResponseObject = DocCardResponseObject(
    id = id.takeIf { it != MkPlcDocCardId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkPlcOwnerId.NONE }?.asString(),
    docType = docCardType.toTransportDocCard(),
    visibility = visibility.toTransportDocCard(),
    permissions = permissionsClient.toTransportDocCard(),
    productId = productId.takeIf { it != MkPlcProductId.NONE }?.asString(),
    lock = lock.takeIf { it != MkPlcDocCardLock.NONE }?.asString()
)

private fun Set<MkPlcDocCardPermissionClient>.toTransportDocCard(): Set<DocCardPermissions>? = this
    .map { it.toTransportDocCard() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkPlcDocCardPermissionClient.toTransportDocCard() = when (this) {
    MkPlcDocCardPermissionClient.READ -> DocCardPermissions.READ
    MkPlcDocCardPermissionClient.UPDATE -> DocCardPermissions.UPDATE
    MkPlcDocCardPermissionClient.MAKE_VISIBLE_OWNER -> DocCardPermissions.MAKE_VISIBLE_OWN
    MkPlcDocCardPermissionClient.MAKE_VISIBLE_GROUP -> DocCardPermissions.MAKE_VISIBLE_GROUP
    MkPlcDocCardPermissionClient.MAKE_VISIBLE_PUBLIC -> DocCardPermissions.MAKE_VISIBLE_PUBLIC
    MkPlcDocCardPermissionClient.DELETE -> DocCardPermissions.DELETE
}

internal fun MkPlcVisibility.toTransportDocCard(): DocCardVisibility? = when (this) {
    MkPlcVisibility.VISIBLE_PUBLIC -> DocCardVisibility.PUBLIC
    MkPlcVisibility.VISIBLE_TO_GROUP -> DocCardVisibility.REGISTERED_ONLY
    MkPlcVisibility.VISIBLE_TO_OWNER -> DocCardVisibility.OWNER_ONLY
    MkPlcVisibility.NONE -> null
}

internal fun MkPlcDocCardType.toTransportDocCard(): DocType? = when (this) {
    MkPlcDocCardType.PDF -> DocType.IMAGE_SLASH_JPEG
    MkPlcDocCardType.PNG -> DocType.IMAGE_SLASH_PNG
    MkPlcDocCardType.JPEG -> DocType.IMAGE_SLASH_JPEG
    MkPlcDocCardType.MS_WORD -> DocType.APPLICATION_SLASH_MSWORD
    MkPlcDocCardType.UNKNOWN -> DocType.APPLICATION_SLASH_PDF
}

private fun List<MkPlcDocCardError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportDocCard() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkPlcDocCardError.toTransportDocCard() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun MkPlcDocCardState.toResult(): ResponseResult? = when (this) {
    MkPlcDocCardState.RUNNING, MkPlcDocCardState.FINISHING -> ResponseResult.SUCCESS
    MkPlcDocCardState.FAILING -> ResponseResult.ERROR
    MkPlcDocCardState.NONE -> null
}
