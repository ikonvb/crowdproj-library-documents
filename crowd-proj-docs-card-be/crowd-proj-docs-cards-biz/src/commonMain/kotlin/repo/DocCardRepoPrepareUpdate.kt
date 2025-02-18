package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.MkPlcDocCardState


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        docCardRepoPrepare = docCardRepoRead.deepCopy().apply {
            this.title = docCardValidated.title
            description = docCardValidated.description
            docCardType = docCardValidated.docCardType
            visibility = docCardValidated.visibility
            lock = docCardValidated.lock
        }
    }
}
