package crowd.proj.docs.cards.pg.repo

import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest

private fun String.toDb() = this.takeIf { it.isNotBlank() }

internal fun MkPlcDocCardId.toDb() = mapOf(
    SqlFields.ID to asString().toDb(),
)

internal fun MkPlcDocCardLock.toDb() = mapOf(
    SqlFields.LOCK_OLD to asString().toDb(),
)

internal fun MkPlcDocCard.toDb() = id.toDb() + mapOf(
    SqlFields.TITLE to title.toDb(),
    SqlFields.DESCRIPTION to description.toDb(),
    SqlFields.OWNER_ID to ownerId.asString().toDb(),
    SqlFields.DOC_TYPE to docCardType.toDb(),
    SqlFields.VISIBILITY to visibility.toDb(),
    SqlFields.PRODUCT_ID to productId.asString().toDb(),
    SqlFields.LOCK to lock.asString().toDb(),
)

internal fun DbDocCardSearchRequest.toDb() = mapOf(
    // Используется для LIKE '%titleFilter%
    SqlFields.FILTER_TITLE to titleFilter.toDb()?.let { "%$it%" },
    SqlFields.FILTER_DOC_TYPE to docCardType.toDb(),
    SqlFields.FILTER_OWNER_ID to ownerId.asString().toDb(),
)

private fun MkPlcDocCardType.toDb() = when (this) {
    MkPlcDocCardType.PDF -> SqlFields.DOC_TYPE_PDF
    MkPlcDocCardType.PNG -> SqlFields.DOC_TYPE_PNG
    MkPlcDocCardType.UNKNOWN -> null
    MkPlcDocCardType.JPEG -> TODO()
    MkPlcDocCardType.MS_WORD -> TODO()
}

private fun MkPlcDocCardVisibility.toDb() = when (this) {
    MkPlcDocCardVisibility.VISIBLE_TO_OWNER -> SqlFields.VISIBILITY_OWNER
    MkPlcDocCardVisibility.VISIBLE_TO_GROUP -> SqlFields.VISIBILITY_GROUP
    MkPlcDocCardVisibility.VISIBLE_PUBLIC -> SqlFields.VISIBILITY_PUBLIC
    MkPlcDocCardVisibility.NONE -> null
}
