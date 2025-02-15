package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardIdRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseErrorWithData
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk

fun CorChainDsl<MkPlcDocCardContext, Unit>.repoDelete(title: String) = worker {

    this.title = title
    description = "Удаление документа из БД по ID"

    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardIdRequest(docCardRepoPrepare)
        when (val result = docCardRepo.deleteDocCard(request)) {
            is DbDocCardResponseOk -> docCardRepoDone = result.data
            is DbDocCardResponseError -> {
                fail(result.errors)
                docCardRepoDone = docCardRepoRead
            }

            is DbDocCardResponseErrorWithData -> {
                fail(result.errors)
                docCardRepoDone = result.data
            }
        }
    }
}
