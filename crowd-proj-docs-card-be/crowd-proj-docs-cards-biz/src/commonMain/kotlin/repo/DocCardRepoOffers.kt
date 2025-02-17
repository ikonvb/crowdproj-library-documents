package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardsResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoOffers(title: String) = worker {
    this.title = title
    description = "Поиск предложений для документа по названию"
    on { state == MkPlcDocCardState.RUNNING }
    handle {

        val docCardRequest = docCardRepoPrepare

        val docCardType = when (docCardRequest.docCardType) {
            MkPlcDocCardType.PDF -> MkPlcDocCardType.PDF
            MkPlcDocCardType.PNG -> MkPlcDocCardType.PNG
            MkPlcDocCardType.JPEG -> MkPlcDocCardType.JPEG
            MkPlcDocCardType.MS_WORD -> MkPlcDocCardType.MS_WORD
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

        val filter = DbDocCardSearchRequest(
            docCardType = docCardType,
            titleFilter = docCardRequest.title,
            ownerId = docCardRequest.ownerId
        )

        when (val dbResponse = docCardRepo.searchDocCard(filter)) {
            is DbDocCardsResponseOk -> docCardsRepoDone = dbResponse.data.toMutableList()
            is DbDocCardsResponseError -> fail(dbResponse.errors)
        }
    }
}
