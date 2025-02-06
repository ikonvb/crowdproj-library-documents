package validation

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState

fun CorChainDsl<MkPlcDocCardContext, Unit>.endValidation(title: String) = worker {
    this.title = "Окончание валидации"
    on { this.state == MkPlcDocCardState.RUNNING }
    handle {
        docCardValidated = docCardValidating
    }
}