package validation

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCardStubSingleton.get()
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
}

fun validationLockTrim(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCardStubSingleton.prepareResult {
            lock = MkPlcDocCardLock(" \n\t 123-234-abc-ABC \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
}

fun validationLockEmpty(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
