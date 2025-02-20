package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import  crowd.proj.docs.cards.common.MkPlcDocCardContext
import  crowd.proj.docs.cards.common.helpers.fail
import  crowd.proj.docs.cards.common.models.MkPlcDocCardError
import  crowd.proj.docs.cards.common.models.MkPlcDocCardState
import  crowd.proj.docs.cards.common.models.MkPlcDocCardType
import  crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import  crowd.proj.docs.cards.common.repo.DbDocCardsResponseError
import  crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoOffers(title: String) = worker {
    this.title = title
    description = "Поиск предложений для документа по названию"
    on { state == MkPlcDocCardState.RUNNING }
    handle {

        val docCardRequest = docCardRepoPrepare

        val filter = DbDocCardSearchRequest(
            docCardType = when (docCardRequest.docCardType) {
                MkPlcDocCardType.PDF -> MkPlcDocCardType.PNG
                MkPlcDocCardType.PNG -> MkPlcDocCardType.PDF
                MkPlcDocCardType.JPEG -> MkPlcDocCardType.MS_WORD
                MkPlcDocCardType.MS_WORD -> MkPlcDocCardType.JPEG
                MkPlcDocCardType.UNKNOWN -> {
                    fail(
                        MkPlcDocCardError(
                            field = "docCardType",
                            message = "Type of docCard must not be empty"
                        )
                    )
                    return@handle
                }
            }
        )

        when (val dbResponse = docCardRepo.searchDocCard(filter)) {
            is DbDocCardsResponseOk -> docCardsRepoDone = dbResponse.data.toMutableList()
            is DbDocCardsResponseError -> fail(dbResponse.errors)
        }
    }
}
