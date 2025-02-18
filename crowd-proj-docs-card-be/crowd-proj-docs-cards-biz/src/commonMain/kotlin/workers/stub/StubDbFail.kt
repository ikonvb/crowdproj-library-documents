package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.fail
import crowd.proj.docs.cards.common.models.MkPlcDocCardError
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubDbError(title: String) =

    worker {

        this.title = title
        this.description = "Имитация ошибки доступа к базе данных"

        on {
            (state == MkPlcDocCardState.RUNNING) && (stubCase == MkPlcDocCardStubs.DB_ERROR)
        }

        handle {
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