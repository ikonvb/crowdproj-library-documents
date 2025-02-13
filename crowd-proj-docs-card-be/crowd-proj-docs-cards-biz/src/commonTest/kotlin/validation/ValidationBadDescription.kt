package validation

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MkPlcDocCardStubSingleton.get()

fun validationDescriptionCorrect(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stub.id,
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
    assertEquals("abc", ctx.docCardValidated.description)
}

fun validationDescriptionTrim(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stub.id,
            title = "abc",
            description = " \n\tabc \n\t",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
    assertEquals("abc", ctx.docCardValidated.description)
}

fun validationDescriptionEmpty(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {

    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stub.id,
            title = "abc",
            description = "",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
