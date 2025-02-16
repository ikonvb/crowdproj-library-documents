package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardLock


data class DbDocCardReadRequest(
    val id: MkPlcDocCardId,
    val lock: MkPlcDocCardLock = MkPlcDocCardLock.NONE,
) {
    constructor(docCard: MkPlcDocCard) : this(docCard.id, docCard.lock)

}
