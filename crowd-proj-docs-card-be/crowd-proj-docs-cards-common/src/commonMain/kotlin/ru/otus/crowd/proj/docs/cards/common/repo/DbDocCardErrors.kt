package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.helpers.errorSystem
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardLock
import ru.otus.crowd.proj.docs.cards.common.repo.exceptions.RepoConcurrencyException
import ru.otus.crowd.proj.docs.cards.common.repo.exceptions.RepoException


const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MkPlcDocCardId) = DbDocCardResponseError(
    MkPlcDocCardError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbDocCardResponseError(
    MkPlcDocCardError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldDocCard: MkPlcDocCard,
    expectedLock: MkPlcDocCardLock,
    exception: Exception = RepoConcurrencyException(
        id = oldDocCard.id,
        expectedLock = expectedLock,
        actualLock = oldDocCard.lock,
    ),
) = DbDocCardResponseErrorWithData(
    docCard = oldDocCard,
    err = MkPlcDocCardError(
        code = "${ERROR_GROUP_REPO}-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldDocCard.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: MkPlcDocCardId) = DbDocCardResponseError(
    MkPlcDocCardError(
        code = "${ERROR_GROUP_REPO}-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for DocCard ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbDocCardResponseError(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)