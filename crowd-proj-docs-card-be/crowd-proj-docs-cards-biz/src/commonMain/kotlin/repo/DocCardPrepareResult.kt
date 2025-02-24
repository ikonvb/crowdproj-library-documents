package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import  crowd.proj.docs.cards.common.MkPlcDocCardContext
import  crowd.proj.docs.cards.common.models.MkPlcDocCardState
import  crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode

fun CorChainDsl<MkPlcDocCardContext, Unit>.prepareResult(title: String) = worker {

    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"

    on { workMode != MkPlcDocCardWorkMode.STUB }

    handle {
        mkPlcDocCardResponse = docCardRepoDone
        mkPlcDocCardsResponse = docCardsRepoDone
        state = when (val st = state) {
            MkPlcDocCardState.RUNNING -> MkPlcDocCardState.FINISHING
            else -> st
        }
    }
}
