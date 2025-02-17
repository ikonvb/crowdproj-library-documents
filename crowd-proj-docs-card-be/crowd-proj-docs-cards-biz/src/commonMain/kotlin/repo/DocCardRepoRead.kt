package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardReadRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseErrorWithData
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk


fun CorChainDsl<MkPlcDocCardContext, Unit>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение документа из БД"
    on { state == MkPlcDocCardState.RUNNING }

    handle {

        println("DbDocCardReadRequest docCardValidated = ${docCardValidated} ")
        println("DbDocCardReadRequest docCardValidating = ${docCardValidating} ")

        val request = DbDocCardReadRequest(docCardValidated)

        println("DbDocCardReadRequest = ${request} ")
        //DbAdIdRequest = DbAdIdRequest(id=MkplAdId(id=666), lock=MkplAdLock(id=123-234-abc-ABC))
        //DbDocCardReadRequest(id=MkPlcDocCardId(id=123-234-abc-ABC), lock=MkPlcDocCardLock(id=123-234-abc-ABC))
        val result = docCardRepo.readDocCard(request)

        println("docCardRepo.readDocCard = ${result} ")

        when (result) {

            is DbDocCardResponseOk -> {
                println("DbDocCardResponseOk result.errors = ${result.data} ")
                docCardRepoRead = result.data
            }

            is DbDocCardResponseError -> {
                println("DbDocCardResponseError result.errors = ${result.errors} ")
                fail(result.errors)
            }

            is DbDocCardResponseErrorWithData -> {
                println("repoRead result.errors = ${result.errors} ")
                fail(result.errors)
                docCardRepoRead = result.data
            }
        }
    }
}
