package validation

import com.crowdproj.kotlin.cor.rootChain
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardFilter
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import validation.fields.validateTitleHasContent
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkPlcDocCardContext(state = MkPlcDocCardState.RUNNING, docCardValidating = MkPlcDocCard(title = ""))
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardValidating = MkPlcDocCard(title = "12!@#$%^&*()_+-=")
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardFilterValidating = MkPlcDocCardFilter(searchString = "Ж")
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
