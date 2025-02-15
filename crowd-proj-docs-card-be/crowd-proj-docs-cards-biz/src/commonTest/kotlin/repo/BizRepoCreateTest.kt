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
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = MkPlcOwnerId("321")
    private val command = MkPlcDocCardCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = DocCardRepositoryMock(
        invokeCreateDocCard = {
            DbDocCardResponseOk(
                data = MkPlcDocCard(
                    id = MkPlcDocCardId(uuid),
                    title = it.docCard.title,
                    description = it.docCard.description,
                    ownerId = userId,
                    docCardType = it.docCard.docCardType,
                    visibility = it.docCard.visibility,
                )
            )
        }
    )
    private val settings = MkPlcCorSettings(
        repoTest = repo
    )
    private val processor = MkPlcDocCardProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MkPlcDocCardContext(
            command = command,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.TEST,
            mkPlcDocCardRequest = MkPlcDocCard(
                title = "abc",
                description = "abc",
                docCardType = MkPlcDocCardType.PDF,
                visibility = MkPlcVisibility.VISIBLE_PUBLIC,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCardState.FINISHING, ctx.state)
        assertNotEquals(MkPlcDocCardId.NONE, ctx.mkPlcDocCardResponse.id)
        assertEquals("abc", ctx.mkPlcDocCardResponse.title)
        assertEquals("abc", ctx.mkPlcDocCardResponse.description)
        assertEquals(MkPlcDocCardType.PDF, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(MkPlcVisibility.VISIBLE_PUBLIC, ctx.mkPlcDocCardResponse.visibility)
    }
}
