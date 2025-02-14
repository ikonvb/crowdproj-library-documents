package chains

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState

fun CorChainDsl<MkPlcDocCardContext, Unit>.chainOperation(
    title: String,
    command: MkPlcDocCardCommand,
    func: CorChainDsl<MkPlcDocCardContext, Unit>.() -> Unit
) = chain {

    this.title = title
    on {
        (this.command == command) && (this.state == MkPlcDocCardState.RUNNING)
    }
    func()

}