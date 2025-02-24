package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.fail
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.repo.DbDocCardDeleteRequest
import crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import crowd.proj.docs.cards.common.repo.DbDocCardResponseErrorWithData
import crowd.proj.docs.cards.common.repo.DbDocCardResponseOk

fun CorChainDsl<MkPlcDocCardContext, Unit>.repoDelete(title: String) = worker {

    this.title = title
    description = "Удаление документа из БД по ID"

    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardDeleteRequest(docCardRepoPrepare)
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
