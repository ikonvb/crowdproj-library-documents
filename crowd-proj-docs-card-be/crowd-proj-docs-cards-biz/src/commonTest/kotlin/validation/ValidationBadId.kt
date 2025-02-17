package validation

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runBizTest {

    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCardStubSingleton.get()
    )

    processor.exec(ctx)

    println("ctx.error = ${ctx.errors}")

    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
}

fun validationIdTrim(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runBizTest {

    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCardStubSingleton.prepareResult {
            id = MkPlcDocCardId(" \n\t ${id.asString()} \n\t ")
        },
    )
    processor.exec(ctx)

    println("ctx.error = ${ctx.errors}")
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
}

fun validationIdEmpty(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runBizTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId(""),
            title = "abc",
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
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: MkPlcDocCardCommand, processor: MkPlcDocCardProcessor) = runBizTest {

    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("!@#\$%^&*(),.{}"),
            title = "abc",
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
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
