package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.fail
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import crowd.proj.docs.cards.common.repo.DbDocCardsResponseError
import crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск документов в БД по фильтру"
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardSearchRequest(
            titleFilter = docCardFilterValidated.searchString,
            ownerId = docCardFilterValidated.ownerId,
            docCardType = docCardFilterValidated.docCardType,
        )
        when (val result = docCardRepo.searchDocCard(request)) {
            is DbDocCardsResponseOk -> docCardsRepoDone = result.data.toMutableList()
            is DbDocCardsResponseError -> fail(result.errors)
        }
    }
}
