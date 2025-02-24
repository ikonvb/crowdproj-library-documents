package crowd.proj.docs.cards.inmemory.repo

import crowd.proj.docs.cards.common.models.*

data class DocCardEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val docCardType: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: MkPlcDocCard) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        docCardType = model.docCardType.takeIf { it != MkPlcDocCardType.UNKNOWN }?.name,
        visibility = model.visibility.takeIf { it != MkPlcDocCardVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = MkPlcDocCard(
        id = id?.let { MkPlcDocCardId(it) } ?: MkPlcDocCardId.NONE,
        title = title ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { MkPlcDocCardOwnerId(it) } ?: MkPlcDocCardOwnerId.NONE,
        docCardType = docCardType?.let { MkPlcDocCardType.valueOf(it) } ?: MkPlcDocCardType.UNKNOWN,
        visibility = visibility?.let { MkPlcDocCardVisibility.valueOf(it) } ?: MkPlcDocCardVisibility.NONE,
        lock = lock?.let { MkPlcDocCardLock(it) } ?: MkPlcDocCardLock.NONE,
    )
}