package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcVisibility
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.logging.common.LogLevel


fun CorChainDsl<MkPlcDocCardContext, Unit>.successCreateStub(title: String, corSettings: MkPlcCorSettings) {

    worker {

        this.title = title
        description = "Успешная обработка запроса по созданию документа"
        on { state == MkPlcDocCardState.RUNNING && stubCase == MkPlcDocCardStubs.SUCCESS }
        val logger = corSettings.loggerProvider.logger("successCreateStub")

        handle {

            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {

                val stub = MkPlcDocCardStubSingleton.prepareResult {
                    mkPlcDocCardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                    mkPlcDocCardRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                    mkPlcDocCardRequest.docCardType.takeIf { it != MkPlcDocCardType.UNKNOWN }
                        ?.also { this.docCardType = it }
                    mkPlcDocCardRequest.visibility.takeIf { it != MkPlcVisibility.NONE }
                        ?.also { this.visibility = it }
                }

                mkPlcDocCardResponse = stub
                state = MkPlcDocCardState.FINISHING

            }
        }
    }
}