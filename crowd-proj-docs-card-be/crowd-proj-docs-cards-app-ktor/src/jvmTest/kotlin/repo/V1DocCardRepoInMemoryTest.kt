package repo

import config.MkPlcAppSettings
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import ru.otus.crowd.proj.docs.be.api.v1.models.DocCardRequestDebugMode
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.repo.IRepoDocCard


class V1DocCardRepoInMemoryTest : V1DocCardRepoBaseTest() {
    override val workMode: DocCardRequestDebugMode = DocCardRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoDocCard) = MkPlcAppSettings(
        corSettings = MkPlcDocCardCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(DocCardRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            DocCardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsUpdate: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            DocCardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsDelete: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            DocCardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsSearch: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            DocCardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsOffers: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            DocCardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDocCard, initDocCardImg),
        )
    )
}
