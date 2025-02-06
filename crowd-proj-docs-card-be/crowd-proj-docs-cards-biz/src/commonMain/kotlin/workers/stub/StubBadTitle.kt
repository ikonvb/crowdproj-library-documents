package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubValidationBadTitle(title: String) {

    worker {

        this.title = title
        on { state == MkPlcDocCardState.RUNNING && stubCase == MkPlcDocCardStubs.BAD_TITLE }

        handle {
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