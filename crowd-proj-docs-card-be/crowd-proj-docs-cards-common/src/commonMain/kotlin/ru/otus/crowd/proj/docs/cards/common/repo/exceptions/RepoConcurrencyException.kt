package ru.otus.crowd.proj.docs.cards.common.repo.exceptions

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardLock


class RepoConcurrencyException(id: MkPlcDocCardId, expectedLock: MkPlcDocCardLock, actualLock: MkPlcDocCardLock?) :
    RepoDocCardException(
        id,
        "Expected lock is $expectedLock while actual lock in db is $actualLock"
    )
