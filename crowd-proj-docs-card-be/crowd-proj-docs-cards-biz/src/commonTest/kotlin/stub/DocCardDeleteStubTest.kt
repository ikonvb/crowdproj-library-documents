package stub

import MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class DocCardDeleteStubTest {

    private val processor = MkPlcDocCardProcessor()
    val id = MkPlcDocCardId("666")

    @Test
    fun delete() = runTest {

        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.DELETE,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.STUB,
            stubCase = MkPlcDocCardStubs.SUCCESS,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = MkPlcDocCardStubSingleton.get()
        assertEquals(stub.id, ctx.mkPlcDocCardResponse.id)
        assertEquals(stub.title, ctx.mkPlcDocCardResponse.title)
        assertEquals(stub.description, ctx.mkPlcDocCardResponse.description)
        assertEquals(stub.docCardType, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(stub.visibility, ctx.mkPlcDocCardResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.DELETE,
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
    fun databaseError() = runTest {
        val ctx = MkPlcDocCardContext(
            command = MkPlcDocCardCommand.DELETE,
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
            command = MkPlcDocCardCommand.DELETE,
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
