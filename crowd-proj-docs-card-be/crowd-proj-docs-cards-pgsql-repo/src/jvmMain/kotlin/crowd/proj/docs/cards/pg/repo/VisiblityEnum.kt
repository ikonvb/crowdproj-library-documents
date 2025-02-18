package crowd.proj.docs.cards.pg.repo

import crowd.proj.docs.cards.common.models.MkPlcDocCardVisibility
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.visibilityEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.VISIBILITY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.VISIBILITY_OWNER -> MkPlcDocCardVisibility.VISIBLE_TO_OWNER
            SqlFields.VISIBILITY_GROUP -> MkPlcDocCardVisibility.VISIBLE_TO_GROUP
            SqlFields.VISIBILITY_PUBLIC -> MkPlcDocCardVisibility.VISIBLE_PUBLIC
            else -> MkPlcDocCardVisibility.NONE
        }
    },
    toDb = { value ->
        when (value) {
            MkPlcDocCardVisibility.VISIBLE_TO_OWNER -> PgVisibilityOwner
            MkPlcDocCardVisibility.VISIBLE_TO_GROUP -> PgVisibilityGroup
            MkPlcDocCardVisibility.VISIBLE_PUBLIC -> PgVisibilityPublic
            MkPlcDocCardVisibility.NONE -> throw Exception("Wrong value of Visibility. NONE is unsupported")
        }
    }
)

sealed class PgVisibilityValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.VISIBILITY_TYPE
        value = eValue
    }
}

object PgVisibilityPublic : PgVisibilityValue(SqlFields.VISIBILITY_PUBLIC) {
    private fun readResolve(): Any = PgVisibilityPublic
}

object PgVisibilityOwner : PgVisibilityValue(SqlFields.VISIBILITY_OWNER) {
    private fun readResolve(): Any = PgVisibilityOwner
}

object PgVisibilityGroup : PgVisibilityValue(SqlFields.VISIBILITY_GROUP) {
    private fun readResolve(): Any = PgVisibilityGroup
}
