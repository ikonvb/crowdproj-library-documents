package validation.fields

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.errorValidation
import crowd.proj.docs.cards.common.helpers.fail

fun CorChainDsl<MkPlcDocCardContext, Unit>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { this.docCardValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "Description is empty",
            )
        )
    }
}