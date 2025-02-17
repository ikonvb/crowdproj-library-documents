package repo

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import ru.otus.crowd.proj.docs.cards.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initDocCard = MkPlcDocCard(
    id = MkPlcDocCardId("123"),
    title = "abc",
    description = "abc",
    docCardType = MkPlcDocCardType.PDF,
    visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
)

private val repo = DocCardRepositoryMock(
    invokeReadDocCard = {
        if (it.id == initDocCard.id) {
            DbDocCardResponseOk(
                data = initDocCard,
            )
        } else errorNotFound(it.id)
    }
)

private val settings = MkPlcDocCardCorSettings(repoTest = repo)
private val processor = MkPlcDocCardProcessor(settings)

fun repoNotFoundTest(command: MkPlcDocCardCommand) = runTest {
    val ctx = MkPlcDocCardContext(
        command = command,
        state = MkPlcDocCardState.NONE,
        workMode = MkPlcDocCardWorkMode.TEST,
        mkPlcDocCardRequest = MkPlcDocCard(
            id = MkPlcDocCardId("12345"),
            title = "xyz",
            description = "xyz",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_TO_GROUP,
            lock = MkPlcDocCardLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(MkPlcDocCardState.FAILING, ctx.state)
    assertEquals(MkPlcDocCard(), ctx.mkPlcDocCardResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
