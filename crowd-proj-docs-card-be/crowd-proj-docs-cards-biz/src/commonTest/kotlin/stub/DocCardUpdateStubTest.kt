package stub

import MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class DocCardUpdateStubTest {

    private val processor = MkPlcDocCardProcessor()
    val id = MkPlcDocCardId("777")
    val title = "title 666"
    val description = "desc 666"
    val dockType = MkPlcDocCardType.PDF
    val visibility = MkPlcVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.UPDATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.SUCCESS,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = title,
                description = description,
                docCardType = dockType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.mkPlcDocCardResponse.id)
        assertEquals(title, ctx.mkPlcDocCardResponse.title)
        assertEquals(description, ctx.mkPlcDocCardResponse.description)
        assertEquals(dockType, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(visibility, ctx.mkPlcDocCardResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.UPDATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_ID,
            mkPlcDocCardRequest = MkPlcDocCard(),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.UPDATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_TITLE,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = "",
                description = description,
                docCardType = dockType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.UPDATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_DESCRIPTION,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = title,
                description = "",
                docCardType = dockType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.UPDATE,
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
            command = MkPlcDocCardCommand.UPDATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_SEARCH_STRING,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = title,
                description = description,
                docCardType = dockType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
