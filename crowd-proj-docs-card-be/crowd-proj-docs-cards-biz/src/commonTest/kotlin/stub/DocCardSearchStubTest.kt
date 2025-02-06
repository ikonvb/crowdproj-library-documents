package stub

import MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DocCardSearchStubTest {

    private val processor = MkPlcDocCardProcessor()
    val filter = MkPlcDocCardFilter(searchString = "test")

    @Test
    fun read() = runTest {

        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.SEARCH,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.SUCCESS,
            mkPlcDocCardFilterRequest = filter,
        )

        processor.exec(ctx)
        assertTrue(ctx.mkPlcDocCardsResponse.size > 1)
        val first = ctx.mkPlcDocCardsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with(MkPlcDocCardStubSingleton.get()) {
            assertEquals(docCardType, first.docCardType)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.SEARCH,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_ID,
            mkPlcDocCardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.SEARCH,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.DB_ERROR,
            mkPlcDocCardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.SEARCH,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_TITLE,
            mkPlcDocCardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
