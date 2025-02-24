package crowd.proj.docs.cards.common.repo.exceptions

import crowd.proj.docs.cards.common.models.MkPlcDocCardId

open class RepoDocCardException(
    @Suppress("unused")
    val docCardId: MkPlcDocCardId,
    msg: String,
) : RepoException(msg)
