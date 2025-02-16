package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.logging.common.LogLevel

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubDeleteSuccess(title: String, corSettings: MkPlcDocCardCorSettings) = worker {

    this.title = title
    this.description = """
        Кейс успеха для удаления документа
    """.trimIndent()
    on { stubCase == MkPlcDocCardStubs.SUCCESS && state == MkPlcDocCardState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")

    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkPlcDocCardState.FINISHING
            val stub = MkPlcDocCardStubSingleton.prepareResult {
                mkPlcDocCardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            mkPlcDocCardResponse = stub
        }
    }
}