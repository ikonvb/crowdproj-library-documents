package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import  crowd.proj.docs.cards.common.MkPlcDocCardContext
import  crowd.proj.docs.cards.common.helpers.fail
import  crowd.proj.docs.cards.common.models.MkPlcDocCardState
import  crowd.proj.docs.cards.common.repo.DbDocCardReadRequest
import  crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import  crowd.proj.docs.cards.common.repo.DbDocCardResponseErrorWithData
import  crowd.proj.docs.cards.common.repo.DbDocCardResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение документа из БД"
    on { state == MkPlcDocCardState.RUNNING }

    handle {

        val request = DbDocCardReadRequest(docCardValidated)
        val result = docCardRepo.readDocCard(request)

        when (result) {

            is DbDocCardResponseOk -> {
                docCardRepoRead = result.data
            }

            is DbDocCardResponseError -> {
                fail(result.errors)
            }

            is DbDocCardResponseErrorWithData -> {
                fail(result.errors)
                docCardRepoRead = result.data
            }
        }
    }
}
