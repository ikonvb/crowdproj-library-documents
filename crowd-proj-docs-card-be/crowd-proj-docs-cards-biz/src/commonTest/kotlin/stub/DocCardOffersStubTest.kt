package stub

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DocCardOffersStubTest {

    private val processor = MkPlcDocCardProcessor()
    val id = MkPlcDocCardId("111")

    @Test
    fun offers() = runTest {

        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.OFFERS,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.SUCCESS,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
            ),
        )
        processor.exec(ctx)

        assertEquals(id, ctx.mkPlcDocCardResponse.id)

        with(MkPlcDocCardStubSingleton.get()) {
            assertEquals(title, ctx.mkPlcDocCardResponse.title)
            assertEquals(description, ctx.mkPlcDocCardResponse.description)
            assertEquals(docCardType, ctx.mkPlcDocCardResponse.docCardType)
            assertEquals(visibility, ctx.mkPlcDocCardResponse.visibility)
        }

        assertTrue(ctx.mkPlcDocCardsResponse.size > 1)
        val first = ctx.mkPlcDocCardsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(ctx.mkPlcDocCardResponse.title))
        assertTrue(first.description.contains(ctx.mkPlcDocCardResponse.title))
        assertEquals(MkPlcDocCardType.PDF, first.docCardType)
        assertEquals(MkPlcDocCardStubSingleton.get().visibility, first.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.OFFERS,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_ID,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.OFFERS,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.DB_ERROR,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.OFFERS,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_TITLE,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
