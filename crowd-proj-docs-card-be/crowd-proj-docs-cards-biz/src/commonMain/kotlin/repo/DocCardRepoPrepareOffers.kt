package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoPrepareOffers(title: String) = worker {
    this.title = title
    description = "Готовим данные к поиску предложений в БД"
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        docCardRepoPrepare = docCardRepoRead.deepCopy()
        docCardRepoDone = docCardRepoRead.deepCopy()
    }
}
