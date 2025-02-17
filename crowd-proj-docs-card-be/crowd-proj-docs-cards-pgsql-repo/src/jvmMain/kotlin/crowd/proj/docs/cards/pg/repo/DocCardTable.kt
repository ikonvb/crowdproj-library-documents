package crowd.proj.docs.cards.pg.repo

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.crowd.proj.docs.cards.common.models.*


class DocCardTable(tableName: String) : Table(tableName) {

    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val docType = docCardTypeEnumeration(SqlFields.DOC_TYPE)
    val lock = text(SqlFields.LOCK)
    val productId = text(SqlFields.PRODUCT_ID).nullable()
    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = MkPlcDocCard(
        id = MkPlcDocCardId(res[id].toString()),
        title = res[title] ?: "",
        description = res[description] ?: "",
        ownerId = MkPlcDocCardOwnerId(res[owner].toString()),
        visibility = res[visibility],
        docCardType = res[docType],
        lock = MkPlcDocCardLock(res[lock]),
        productId = res[productId]?.let { MkPlcDocCardProductId(it) } ?: MkPlcDocCardProductId.NONE,
    )

    fun to(it: UpdateBuilder<*>, docCard: MkPlcDocCard, randomUuid: () -> String) {
        it[id] = docCard.id.takeIf { it != MkPlcDocCardId.NONE }?.asString() ?: randomUuid()
        it[title] = docCard.title
        it[description] = docCard.description
        it[owner] = docCard.ownerId.asString()
        it[visibility] = docCard.visibility
        it[docType] = docCard.docCardType
        it[lock] = docCard.lock.takeIf { it != MkPlcDocCardLock.NONE }?.asString() ?: randomUuid()
        it[productId] = docCard.productId.takeIf { it != MkPlcDocCardProductId.NONE }?.asString()
    }

}

