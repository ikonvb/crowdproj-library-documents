package crowd.proj.docs.cards.pg.repo

import io.github.moreirasantos.pgkn.resultset.ResultSet
import ru.otus.crowd.proj.docs.cards.common.models.*

internal fun ResultSet.fromDb(cols: List<String>): MkPlcDocCard {
    val fieldsMap = cols.mapIndexed { i: Int, field: String -> field to i }.toMap()
    fun col(field: String): String? = fieldsMap[field]?.let { getString(it) }
    return MkPlcDocCard(
        id = col(SqlFields.ID)?.let { MkPlcDocCardId(it) } ?: MkPlcDocCardId.NONE,
        title = col(SqlFields.TITLE) ?: "",
        description = col(SqlFields.DESCRIPTION) ?: "",
        ownerId = col(SqlFields.OWNER_ID)?.let { MkPlcDocCardOwnerId(it) } ?: MkPlcDocCardOwnerId.NONE,
        docCardType = col(SqlFields.DOC_TYPE).asDocCardType(),
        visibility = col(SqlFields.VISIBILITY).asVisibility(),
        productId = col(SqlFields.PRODUCT_ID)?.let { MkPlcDocCardProductId(it) } ?: MkPlcDocCardProductId.NONE,
        lock = col(SqlFields.LOCK)?.let { MkPlcDocCardLock(it) } ?: MkPlcDocCardLock.NONE,
    )
}

private fun String?.asDocCardType(): MkPlcDocCardType = when (this) {
    SqlFields.DOC_TYPE_PDF -> MkPlcDocCardType.PDF
    SqlFields.DOC_TYPE_PNG -> MkPlcDocCardType.PNG
    else -> MkPlcDocCardType.UNKNOWN
}

private fun String?.asVisibility(): MkPlcDocCardVisibility = when (this) {
    SqlFields.VISIBILITY_OWNER -> MkPlcDocCardVisibility.VISIBLE_TO_OWNER
    SqlFields.VISIBILITY_GROUP -> MkPlcDocCardVisibility.VISIBLE_TO_GROUP
    SqlFields.VISIBILITY_PUBLIC -> MkPlcDocCardVisibility.VISIBLE_PUBLIC
    else -> MkPlcDocCardVisibility.NONE
}
