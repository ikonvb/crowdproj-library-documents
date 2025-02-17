package crowd.proj.docs.cards.pg.repo

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType

fun Table.docCardTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.DOC_TYPE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.DOC_TYPE_PDF -> MkPlcDocCardType.PDF
            SqlFields.DOC_TYPE_PNG -> MkPlcDocCardType.PNG
            else -> MkPlcDocCardType.UNKNOWN
        }
    },
    toDb = { value ->
        when (value) {
            MkPlcDocCardType.PDF -> PgDocCardTypePDF
            MkPlcDocCardType.PNG -> PgDocCArdTypePNG
            MkPlcDocCardType.JPEG -> PgDocCArdTypeJPEG
            MkPlcDocCardType.MS_WORD -> PgDocCArdTypePNG
            MkPlcDocCardType.UNKNOWN -> throw Exception("Wrong value of doc card Type. UNKNOWN is unsupported")
        }
    }
)

sealed class PgDocCardTypeValue(enVal: String) : PGobject() {
    init {
        type = SqlFields.DOC_TYPE_TYPE
        value = enVal
    }
}

object PgDocCardTypePDF : PgDocCardTypeValue(SqlFields.DOC_TYPE_PDF) {
    private fun readResolve(): Any = PgDocCardTypePDF
}

object PgDocCArdTypePNG : PgDocCardTypeValue(SqlFields.DOC_TYPE_PNG) {
    private fun readResolve(): Any = PgDocCArdTypePNG
}

object PgDocCArdTypeJPEG : PgDocCardTypeValue(SqlFields.DOC_TYPE_JPEG) {
    private fun readResolve(): Any = PgDocCArdTypeJPEG
}

object PgDocCArdTypeMSWORD : PgDocCardTypeValue(SqlFields.DOC_TYPE_MS_WORD) {
    private fun readResolve(): Any = PgDocCArdTypeMSWORD
}
