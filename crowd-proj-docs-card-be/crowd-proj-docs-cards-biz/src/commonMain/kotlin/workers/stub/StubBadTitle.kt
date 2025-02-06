package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import ru.otus.crowd.proj.logging.common.LogLevel

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubValidationBadTitle(title: String, corSettings: MkPlcCorSettings) {

    worker {

        this.title = title
        on { state == MkPlcDocCardState.RUNNING && stubCase == MkPlcDocCardStubs.BAD_TITLE }

        val logger = corSettings.loggerProvider.logger("stubValidationBadTitle")

        handle {

            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
                fail(
                    MkPlcDocCardError(
                        group = "validation",
                        code = "validation-title",
                        field = "title",
                        message = "Incorrect title",
                    )
                )
            }
        }
    }

}