package plugins

import config.MkPlcAppSettings
import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import crowd.proj.docs.cards.stubs.repo.DocCardRepoStub
import io.ktor.server.application.Application
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings

fun Application.initAppSettings(): MkPlcAppSettings {
    val corSettings = MkPlcCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = DocCardRepoInMemory(),
        repoProd = DocCardRepoInMemory(),
        repoStub = DocCardRepoStub(),
    )
    return MkPlcAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MkPlcDocCardProcessor(corSettings),
    )
}