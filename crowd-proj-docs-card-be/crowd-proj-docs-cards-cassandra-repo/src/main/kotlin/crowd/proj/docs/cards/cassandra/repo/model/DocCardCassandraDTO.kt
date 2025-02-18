package crowd.proj.docs.cards.cassandra.repo.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import crowd.proj.docs.cards.common.models.*

@Entity
data class DocCardCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey
    var id: String? = null,
    @field:CqlName(COLUMN_TITLE)
    var title: String? = null,
    @field:CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,
    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,
    @field:CqlName(COLUMN_VISIBILITY)
    var visibility: DocCardVisibility? = null,
    @field:CqlName(COLUMN_PRODUCT)
    var productId: String? = null,
    @field:CqlName(COLUMN_DOC_CARD_TYPE)
    var docType: DocType? = null,
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(docCardModel: MkPlcDocCard) : this(
        ownerId = docCardModel.ownerId.takeIf { it != MkPlcDocCardOwnerId.NONE }?.asString(),
        id = docCardModel.id.takeIf { it != MkPlcDocCardId.NONE }?.asString(),
        title = docCardModel.title.takeIf { it.isNotBlank() },

        description = docCardModel.description.takeIf { it.isNotBlank() },

        visibility = docCardModel.visibility.toTransport(),
        productId = docCardModel.productId.takeIf { it != MkPlcDocCardProductId.NONE }?.asString(),
        docType = docCardModel.docCardType.toTransport(),
        lock = docCardModel.lock.takeIf { it != MkPlcDocCardLock.NONE }?.asString()
    )

    fun toDocCardModel(): MkPlcDocCard =
        MkPlcDocCard(
            ownerId = ownerId?.let { MkPlcDocCardOwnerId(it) } ?: MkPlcDocCardOwnerId.NONE,
            id = id?.let { MkPlcDocCardId(it) } ?: MkPlcDocCardId.NONE,
            title = title ?: "",
            description = description ?: "",
            visibility = visibility.fromTransport(),
            productId = productId?.let { MkPlcDocCardProductId(it) } ?: MkPlcDocCardProductId.NONE,
            docCardType = docType.fromTransport(),
            lock = lock?.let { MkPlcDocCardLock(it) } ?: MkPlcDocCardLock.NONE
        )

    companion object {
        const val TABLE_NAME = "doc_cards"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_OWNER_ID = "owner_id_my"
        const val COLUMN_VISIBILITY = "visibility"
        const val COLUMN_PRODUCT = "product"
        const val COLUMN_DOC_CARD_TYPE = "doc_type"
        const val COLUMN_LOCK = "lock"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_TITLE, DataTypes.TEXT)
                .withColumn(COLUMN_DESCRIPTION, DataTypes.TEXT)
                .withColumn(COLUMN_OWNER_ID, DataTypes.TEXT)
                .withColumn(COLUMN_VISIBILITY, DataTypes.TEXT)
                .withColumn(COLUMN_PRODUCT, DataTypes.TEXT)
                .withColumn(COLUMN_DOC_CARD_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()

        fun titleIndex(keyspace: String, tableName: String, locale: String = "en") =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_TITLE)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
    }
}
