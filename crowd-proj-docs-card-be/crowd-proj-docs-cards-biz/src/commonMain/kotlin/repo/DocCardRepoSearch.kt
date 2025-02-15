package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardFilterRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardsResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск документов в БД по фильтру"
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardFilterRequest(
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
