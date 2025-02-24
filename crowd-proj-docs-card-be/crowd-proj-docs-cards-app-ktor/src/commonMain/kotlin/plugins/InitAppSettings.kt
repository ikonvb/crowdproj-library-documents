package plugins

import config.MkPlcAppSettings
import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.stubs.repo.DocCardRepoStub
import io.ktor.server.application.*
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings

fun Application.initAppSettings(): MkPlcAppSettings {
    val corSettings = MkPlcDocCardCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(DocCardDbType.TEST),
        repoProd = getDatabaseConf(DocCardDbType.PROD),
        repoStub = DocCardRepoStub(),
    )
    return MkPlcAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MkPlcDocCardProcessor(corSettings),
    )
}