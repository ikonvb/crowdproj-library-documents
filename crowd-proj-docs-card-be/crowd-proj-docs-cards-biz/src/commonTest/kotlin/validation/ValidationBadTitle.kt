package validation

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stubDocCard = MkPlcDocCardStubSingleton.get()

fun validationTitleCorrect(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {

    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stubDocCard.id,
            title = "abc",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
    assertEquals("abc", ctx.docCardValidated.title)
}

fun validationTitleTrim(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stubDocCard.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
    assertEquals("abc", ctx.docCardValidated.title)
}

fun validationTitleEmpty(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {

    val ctx = MkPlcDocCardContext(
        command = command, // create
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = stubDocCard.id,
            title = "",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("123"),
            title = "!@#$%^&*(),.{}",
            description = "abc",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
            lock = MkPlcDocCardLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
