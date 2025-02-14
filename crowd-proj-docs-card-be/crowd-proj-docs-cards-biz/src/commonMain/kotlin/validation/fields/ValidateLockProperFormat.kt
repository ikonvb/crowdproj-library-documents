package validation.fields

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.errorValidation
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardLock


fun CorChainDsl<MkPlcDocCardContext, Unit>.validateLockProperFormat(title: String) = worker {

    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { docCardValidating.lock != MkPlcDocCardLock.NONE && !docCardValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = docCardValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}