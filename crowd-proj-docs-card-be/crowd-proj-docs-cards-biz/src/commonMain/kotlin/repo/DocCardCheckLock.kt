package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.fail
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.repo.errorRepoConcurrency

fun CorChainDsl<MkPlcDocCardContext, Unit>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == MkPlcDocCardState.RUNNING && docCardValidated.lock != docCardRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(docCardRepoRead, docCardValidated.lock).errors)
    }
}
