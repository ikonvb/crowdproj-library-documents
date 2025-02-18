package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.logging.common.LogLevel

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubReadSuccess(title: String, corSettings: MkPlcDocCardCorSettings) =
    worker {

        this.title = title
        this.description = """
        Кейс успеха для чтения документа
    """.trimIndent()

        on { stubCase == MkPlcDocCardStubs.SUCCESS && state == MkPlcDocCardState.RUNNING }
        val logger = corSettings.loggerProvider.logger("stubReadSuccess")

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