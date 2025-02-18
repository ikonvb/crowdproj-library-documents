package crowd.proj.docs.cards.common.repo.exceptions

import crowd.proj.docs.cards.common.models.MkPlcDocCardId


class RepoEmptyLockException(id: MkPlcDocCardId) : RepoDocCardException(
    id,
    "Lock is empty in DB"
)
