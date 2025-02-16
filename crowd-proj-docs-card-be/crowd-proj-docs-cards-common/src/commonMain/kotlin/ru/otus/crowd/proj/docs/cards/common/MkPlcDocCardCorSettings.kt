package ru.otus.crowd.proj.docs.cards.common

import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard
import ru.otus.crowd.proj.logging.common.MkPlcLoggerProvider

data class MkPlcDocCardCorSettings(
    val loggerProvider: MkPlcLoggerProvider = MkPlcLoggerProvider(),
    val repoStub: IRepoDocCard = IRepoDocCard.NONE,
    val repoTest: IRepoDocCard = IRepoDocCard.NONE,
    val repoProd: IRepoDocCard = IRepoDocCard.NONE,
) {
    companion object {
        val NONE = MkPlcDocCardCorSettings()
    }
}
