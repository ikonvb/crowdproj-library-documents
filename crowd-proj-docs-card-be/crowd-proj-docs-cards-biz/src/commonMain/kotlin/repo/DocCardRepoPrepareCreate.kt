package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardOwnerId


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        docCardRepoPrepare = docCardValidated.deepCopy()
        docCardRepoPrepare.ownerId = MkPlcDocCardOwnerId.NONE
    }
}
