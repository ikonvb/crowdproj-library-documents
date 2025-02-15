package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseErrorWithData
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardRequest(docCardRepoPrepare)
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
