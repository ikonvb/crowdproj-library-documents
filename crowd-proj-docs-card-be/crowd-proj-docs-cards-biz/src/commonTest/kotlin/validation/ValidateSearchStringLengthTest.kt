package validation

import com.crowdproj.kotlin.cor.rootChain
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardFilter
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import validation.fields.validateSearchStringLength
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardFilterValidating = MkPlcDocCardFilter(searchString = "")
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardFilterValidating = MkPlcDocCardFilter(searchString = "  ")
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardFilterValidating = MkPlcDocCardFilter(searchString = "12")
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardFilterValidating = MkPlcDocCardFilter(searchString = "123")
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MkPlcDocCardContext(
            state = MkPlcDocCardState.RUNNING,
            docCardFilterValidating = MkPlcDocCardFilter(searchString = "12".repeat(51))
        )
        chain.exec(ctx)
        assertEquals(MkPlcDocCardState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
