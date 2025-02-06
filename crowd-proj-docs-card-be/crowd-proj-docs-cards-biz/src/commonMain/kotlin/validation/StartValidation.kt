package validation

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState

fun CorChainDsl<MkPlcDocCardContext, Unit>.startValidation(func: CorChainDsl<MkPlcDocCardContext, Unit>.() -> Unit) = chain {
    func()
    endValidation("Окончание валидации")
    this.title = "Валидация"
    on { this.state == MkPlcDocCardState.RUNNING }
}