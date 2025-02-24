package crowd.proj.docs.cards.cassandra.repo.model

import crowd.proj.docs.cards.common.models.MkPlcDocCardVisibility


enum class DocCardVisibility {
    VISIBLE_TO_OWNER,
    VISIBLE_TO_GROUP,
    VISIBLE_PUBLIC,
}

fun DocCardVisibility?.fromTransport() = when (this) {
    null -> MkPlcDocCardVisibility.NONE
    DocCardVisibility.VISIBLE_TO_OWNER -> MkPlcDocCardVisibility.VISIBLE_TO_OWNER
    DocCardVisibility.VISIBLE_TO_GROUP -> MkPlcDocCardVisibility.VISIBLE_TO_GROUP
    DocCardVisibility.VISIBLE_PUBLIC -> MkPlcDocCardVisibility.VISIBLE_PUBLIC
}

fun MkPlcDocCardVisibility.toTransport() = when (this) {
    MkPlcDocCardVisibility.NONE -> null
    MkPlcDocCardVisibility.VISIBLE_TO_OWNER -> DocCardVisibility.VISIBLE_TO_OWNER
    MkPlcDocCardVisibility.VISIBLE_TO_GROUP -> DocCardVisibility.VISIBLE_TO_GROUP
    MkPlcDocCardVisibility.VISIBLE_PUBLIC -> DocCardVisibility.VISIBLE_PUBLIC
}
