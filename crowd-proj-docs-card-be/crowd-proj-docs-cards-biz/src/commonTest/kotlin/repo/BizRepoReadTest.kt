package repo

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = MkPlcOwnerId("321")
    private val command = MkPlcDocCardCommand.READ
    private val initDocCard = MkPlcDocCard(
        id = MkPlcDocCardId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        docCardType = MkPlcDocCardType.PDF,
        visibility = MkPlcVisibility.VISIBLE_PUBLIC,
    )
    private val repo = DocCardRepositoryMock(
        invokeReadDocCard = {
            DbDocCardResponseOk(
                data = initDocCard,
            )
        }
    )
    private val settings = MkPlcCorSettings(repoTest = repo)
    private val processor = MkPlcDocCardProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MkPlcDocCardContext(
            command = command,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.TEST,
            mkPlcDocCardRequest = MkPlcDocCard(
                id = MkPlcDocCardId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCardState.FINISHING, ctx.state)
        assertEquals(initDocCard.id, ctx.mkPlcDocCardResponse.id)
        assertEquals(initDocCard.title, ctx.mkPlcDocCardResponse.title)
        assertEquals(initDocCard.description, ctx.mkPlcDocCardResponse.description)
        assertEquals(initDocCard.docCardType, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(initDocCard.visibility, ctx.mkPlcDocCardResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
