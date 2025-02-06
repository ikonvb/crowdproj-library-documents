package validation.fields

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.errorValidation
import ru.otus.crowd.proj.docs.cards.common.helpers.fail
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId

fun CorChainDsl<MkPlcDocCardContext, Unit>.validateIdProperFormat(title: String) = worker {

    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")

    on { docCardValidating.id != MkPlcDocCardId.NONE && ! docCardValidating.id.asString().matches(regExp) }

    handle {
        val encodedId = docCardValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}