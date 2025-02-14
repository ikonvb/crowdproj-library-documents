package validation.fields

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.errorValidation
import ru.otus.crowd.proj.docs.cards.common.helpers.fail

fun CorChainDsl<MkPlcDocCardContext, Unit>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { this.docCardValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "Title is empty",
            )
        )
    }
}