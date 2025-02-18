package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.models.MkPlcDocCardType
import crowd.proj.docs.cards.common.models.MkPlcDocCardVisibility
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton

import ru.otus.crowd.proj.logging.common.LogLevel


fun CorChainDsl<MkPlcDocCardContext, Unit>.successCreateStub(title: String, corSettings: MkPlcDocCardCorSettings) {

    worker {

        this.title = title
        description = "Успешная обработка запроса по созданию документа"
        on { state == MkPlcDocCardState.RUNNING && stubCase == MkPlcDocCardStubs.SUCCESS }
        val logger = corSettings.loggerProvider.logger("successCreateStub")

        handle {

            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {

                state = MkPlcDocCardState.FINISHING

                val stub = MkPlcDocCardStubSingleton.prepareResult {
                    mkPlcDocCardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                    mkPlcDocCardRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                    mkPlcDocCardRequest.docCardType.takeIf { it != MkPlcDocCardType.UNKNOWN }
                        ?.also { this.docCardType = it }
                    mkPlcDocCardRequest.visibility.takeIf { it != MkPlcDocCardVisibility.NONE }
                        ?.also { this.visibility = it }
                }

                mkPlcDocCardResponse = stub

            }
        }
    }
}