package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId


data class DbDocCardIdRequest(
    val id: MkPlcDocCardId,
) {
    constructor(docCard: MkPlcDocCard) : this(docCard.id)
}
