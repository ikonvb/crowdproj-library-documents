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


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение документа из БД"
    on { state == MkPlcDocCardState.RUNNING }
    handle {
        val request = DbDocCardIdRequest(docCardValidated)
        when (val result = docCardRepo.readDocCard(request)) {
            is DbDocCardResponseOk -> docCardRepoRead = result.data
            is DbDocCardResponseError -> fail(result.errors)
            is DbDocCardResponseErrorWithData -> {
                fail(result.errors)
                docCardRepoRead = result.data
            }
        }
    }
}
