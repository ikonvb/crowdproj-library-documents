package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import  crowd.proj.docs.cards.common.MkPlcDocCardContext
import  crowd.proj.docs.cards.common.helpers.fail
import  crowd.proj.docs.cards.common.models.MkPlcDocCardState
import  crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import  crowd.proj.docs.cards.common.repo.DbDocCardResponseErrorWithData
import  crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import  crowd.proj.docs.cards.common.repo.DbDocCardUpdateRequest


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardUpdateRequest(docCardRepoPrepare)
        when (val result = docCardRepo.updateDocCard(request)) {
            is DbDocCardResponseOk -> docCardRepoDone = result.data
            is DbDocCardResponseError -> fail(result.errors)
            is DbDocCardResponseErrorWithData -> {
                fail(result.errors)
                docCardRepoDone = result.data
            }
        }
    }
}
