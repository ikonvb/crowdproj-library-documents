package workers.init

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState

fun CorChainDsl<MkPlcDocCardContext, Unit>.initStatus(title: String) {
    worker {
        this.title = title
        on {
            state == MkPlcDocCardState.NONE
        }
        handle { state = MkPlcDocCardState.RUNNING }
    }
}