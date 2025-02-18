package workers.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.fail
import crowd.proj.docs.cards.common.models.MkPlcDocCardError
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

fun CorChainDsl<MkPlcDocCardContext, Unit>.stubValidationBadId(title: String) = worker {

    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора документа
    """.trimIndent()

    on { stubCase == MkPlcDocCardStubs.BAD_ID && state == MkPlcDocCardState.RUNNING }

    handle {
        fail(
            MkPlcDocCardError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}