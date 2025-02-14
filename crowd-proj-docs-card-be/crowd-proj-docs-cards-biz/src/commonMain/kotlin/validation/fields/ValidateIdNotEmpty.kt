package validation.fields

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.errorValidation
import ru.otus.crowd.proj.docs.cards.common.helpers.fail

fun CorChainDsl<MkPlcDocCardContext, Unit>.validateIdNotEmpty(title: String) = worker {

    this.title = title
    on { docCardValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}