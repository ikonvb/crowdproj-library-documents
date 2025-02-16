import kotlinx.datetime.Clock
import ru.otus.crowd.proj.docs.be.api.logV1.models.*
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*


fun MkPlcDocCardContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-mk-plc-doc-cards",
    docCard = toMkPlcLog(),
    errors = errors.map { it.toLog() },
)

private fun MkPlcDocCardContext.toMkPlcLog(): MkPlcDocCardLogModel? {
    val docCardNone = MkPlcDocCard()
    return MkPlcDocCardLogModel(
        requestId = requestId.takeIf { it != MkPlcDocCardRequestId.NONE }?.asString(),
        requestDocCard = mkPlcDocCardRequest.takeIf { it != docCardNone }?.toLog(),
        responseDocCard = mkPlcDocCardResponse.takeIf { it != docCardNone }?.toLog(),
        responseDocCards = mkPlcDocCardsResponse.takeIf { it.isNotEmpty() }?.filter { it != docCardNone }
            ?.map { it.toLog() },
        requestFilter = mkPlcDocCardFilterRequest.takeIf { it != MkPlcDocCardFilter() }?.toLog(),
    ).takeIf { it != MkPlcDocCardLogModel() }
}

private fun MkPlcDocCardFilter.toLog() = DocCardFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkPlcDocCardOwnerId.NONE }?.asString(),
    docType = docCardType.takeIf { it != MkPlcDocCardType.UNKNOWN }?.name,
)

private fun MkPlcDocCardError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun MkPlcDocCard.toLog() = DocCardLog(
    id = id.takeIf { it != MkPlcDocCardId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    visibility = visibility.takeIf { it != MkPlcDocCardVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != MkPlcDocCardOwnerId.NONE }?.asString(),
    productId = productId.takeIf { it != MkPlcDocCardProductId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)