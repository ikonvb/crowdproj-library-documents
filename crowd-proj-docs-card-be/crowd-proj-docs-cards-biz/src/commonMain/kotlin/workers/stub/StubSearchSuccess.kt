package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.logging.common.LogLevel

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubSearchSuccess(title: String, corSettings: MkPlcCorSettings) = worker {

    this.title = title
    this.description = """
        Кейс успеха для поиска документов
    """.trimIndent()

    on { stubCase == MkPlcDocCardStubs.SUCCESS && state == MkPlcDocCardState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkPlcDocCardState.FINISHING
            mkPlcDocCardsResponse.addAll(
                MkPlcDocCardStubSingleton.prepareSearchList(
                    mkPlcDocCardFilterRequest.searchString,
                    MkPlcDocCardType.UNKNOWN
                )
            )
        }
    }
}