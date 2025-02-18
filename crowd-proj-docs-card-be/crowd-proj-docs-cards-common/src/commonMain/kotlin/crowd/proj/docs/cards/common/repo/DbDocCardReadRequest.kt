package crowd.proj.docs.cards.common.repo

import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardId
import crowd.proj.docs.cards.common.models.MkPlcDocCardLock


data class DbDocCardReadRequest(
    val id: MkPlcDocCardId,
    val lock: MkPlcDocCardLock = MkPlcDocCardLock.NONE,
) {
    constructor(docCard: MkPlcDocCard) : this(docCard.id, docCard.lock)

}
