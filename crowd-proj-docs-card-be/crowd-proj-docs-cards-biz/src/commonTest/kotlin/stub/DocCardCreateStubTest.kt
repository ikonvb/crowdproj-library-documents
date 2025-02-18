package stub

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class DocCardCreateStubTest {

    private val processor = MkPlcDocCardProcessor()
    val id = MkPlcDocCardId("111")
    val title = "title 111"
    val description = "desc 111"
    val docType = MkPlcDocCardType.PDF
    val visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.CREATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.SUCCESS,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = title,
                description = description,
                docCardType = docType,
                visibility = visibility,
            ),
        )

        processor.exec(ctx)
        assertEquals(MkPlcDocCardStubSingleton.get().id, ctx.mkPlcDocCardResponse.id)
        assertEquals(title, ctx.mkPlcDocCardResponse.title)
        assertEquals(description, ctx.mkPlcDocCardResponse.description)
        assertEquals(docType, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(visibility, ctx.mkPlcDocCardResponse.visibility)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.CREATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_TITLE,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = "",
                description = description,
                docCardType = docType,
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
            command = MkPlcDocCardCommand.CREATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_DESCRIPTION,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = title,
                description = "",
                docCardType = docType,
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
            command = MkPlcDocCardCommand.CREATE,
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
            command = MkPlcDocCardCommand.CREATE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.BAD_ID,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
                title = title,
                description = description,
                docCardType = docType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}

