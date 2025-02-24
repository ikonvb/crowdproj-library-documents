package validation.fields

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.helpers.errorValidation
import crowd.proj.docs.cards.common.helpers.fail


fun CorChainDsl<MkPlcDocCardContext, Unit>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { docCardValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}