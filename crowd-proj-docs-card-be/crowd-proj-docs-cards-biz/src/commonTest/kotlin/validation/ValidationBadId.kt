package validation

import MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
}

fun validationIdTrim(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
}

fun validationIdEmpty(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId(""),
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {

    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
