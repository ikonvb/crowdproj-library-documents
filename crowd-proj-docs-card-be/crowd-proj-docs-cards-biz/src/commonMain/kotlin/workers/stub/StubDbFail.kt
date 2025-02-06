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

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubDbError(title: String, corSettings: MkPlcCorSettings) =

    worker {

        this.title = title
        this.description = "Имитация ошибки доступа к базе данных"

        on {
            (state == MkPlcDocCardState.RUNNING) && (stubCase == MkPlcDocCardStubs.DB_ERROR)
        }
        val logger = corSettings.loggerProvider.logger("stubDbError")
        handle {
            logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
                fail(
                    MkPlcDocCardError(
                        group = "internal",
                        code = "internal-db",
                        field = "description",
                        message = "Internal error",
                    )
                )
            }
        }
    }