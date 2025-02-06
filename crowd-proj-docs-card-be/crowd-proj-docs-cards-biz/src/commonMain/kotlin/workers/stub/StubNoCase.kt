package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState

fun CorChainDsl<MkPlcDocCardContext, Unit>.failStubNoCase(title: String) =

    worker {

        this.title = title
        this.description = "валидация запросов от клиента при запросе неизвестной заглушки"

        on {
            (state == MkPlcDocCardState.RUNNING)
        }

        handle {
            fail(
                MkPlcDocCardError(
                    group = "validation",
                    code = "validation",
                    field = "stub",
                    message = "Wrong stub case is requested: ${stubCase.name}",
                )
            )
        }
    }