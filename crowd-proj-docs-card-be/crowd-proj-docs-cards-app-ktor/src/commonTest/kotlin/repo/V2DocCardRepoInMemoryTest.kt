package repo

import config.MkPlcAppSettings
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardRequestDebugMode
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard


class V2DocCardRepoInMemoryTest : V2DocCardRepoBaseTest() {

    override val workMode: DocCardRequestDebugMode = DocCardRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoDocCard) = MkPlcAppSettings(
        corSettings = MkPlcCorSettings(
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
            initObjects = listOf(initDocCard, initDocCardPdf),
        )
    )
}
