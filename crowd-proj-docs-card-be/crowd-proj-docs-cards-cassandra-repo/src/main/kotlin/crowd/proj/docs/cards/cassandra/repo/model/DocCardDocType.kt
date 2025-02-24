package crowd.proj.docs.cards.cassandra.repo.model

import crowd.proj.docs.cards.common.models.MkPlcDocCardType


enum class DocType {
    PDF,
    PNG,
    JPEG,
    MS_WORD
}

fun DocType?.fromTransport() = when (this) {
    null -> MkPlcDocCardType.UNKNOWN
    DocType.PDF -> MkPlcDocCardType.PDF
    DocType.PNG -> MkPlcDocCardType.PNG
    DocType.JPEG -> MkPlcDocCardType.JPEG
    DocType.MS_WORD -> MkPlcDocCardType.MS_WORD
}

fun MkPlcDocCardType.toTransport() = when (this) {
    MkPlcDocCardType.UNKNOWN -> null
    MkPlcDocCardType.PDF -> DocType.PDF
    MkPlcDocCardType.PNG -> DocType.PNG
    MkPlcDocCardType.JPEG -> DocType.JPEG
    MkPlcDocCardType.MS_WORD -> DocType.MS_WORD
}

