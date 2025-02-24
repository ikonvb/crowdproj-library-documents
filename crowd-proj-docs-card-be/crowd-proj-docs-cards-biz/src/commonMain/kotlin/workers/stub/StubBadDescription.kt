package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.fail
import crowd.proj.docs.cards.common.models.MkPlcDocCardError
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubValidationBadDescription(title: String) =

    worker {

        this.title = title
        on { state == MkPlcDocCardState.RUNNING && stubCase == MkPlcDocCardStubs.BAD_DESCRIPTION }

        handle {
            fail(
                MkPlcDocCardError(
                    group = "validation",
                    code = "validation-description",
                    field = "description",
                    message = "Incorrect description",
                )
            )
        }
    }
