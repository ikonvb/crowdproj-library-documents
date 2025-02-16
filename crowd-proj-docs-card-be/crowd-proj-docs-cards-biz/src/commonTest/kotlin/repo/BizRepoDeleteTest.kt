package repo

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = MkPlcDocCardOwnerId("321")
    private val command = MkPlcDocCardCommand.DELETE

    private val initDocCard = MkPlcDocCard(
        id = MkPlcDocCardId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        docCardType = MkPlcDocCardType.PDF,
        visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
        lock = MkPlcDocCardLock("123-456-789")
    )

    private val repo = DocCardRepositoryMock(
        invokeReadDocCard = {
            DbDocCardResponseOk(
                data = initDocCard,
            )
        },
        invokeDeleteDocCard = {
            if (it.id == initDocCard.id)
                DbDocCardResponseOk(
                    data = initDocCard
                )
            else DbDocCardResponseError()
        }
    )
    private val settings by lazy {
        MkPlcDocCardCorSettings(
            repoTest = repo
        )
    }
    private val processor = MkPlcDocCardProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val docCardToUpdate = MkPlcDocCard(
            id = MkPlcDocCardId("123"),
            lock = MkPlcDocCardLock("123-456-789")
        )
        val ctx = MkPlcDocCardContext(
            command = command,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.TEST,
            mkPlcDocCardRequest = docCardToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCardState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initDocCard.id, ctx.mkPlcDocCardResponse.id)
        assertEquals(initDocCard.title, ctx.mkPlcDocCardResponse.title)
        assertEquals(initDocCard.description, ctx.mkPlcDocCardResponse.description)
        assertEquals(initDocCard.docCardType, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(initDocCard.visibility, ctx.mkPlcDocCardResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
