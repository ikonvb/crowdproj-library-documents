package repo

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = MkPlcDocCardOwnerId("321")
    private val command = MkPlcDocCardCommand.UPDATE
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
        invokeUpdateDocCard = {
            DbDocCardResponseOk(
                data = MkPlcDocCard(
                    id = MkPlcDocCardId("123"),
                    title = "xyz",
                    description = "xyz",
                    docCardType = MkPlcDocCardType.PDF,
                    visibility = MkPlcDocCardVisibility.VISIBLE_TO_GROUP,
                    lock = MkPlcDocCardLock("123-456-789")
                )
            )
        }
    )

    private val settings = MkPlcDocCardCorSettings(repoTest = repo)
    private val processor = MkPlcDocCardProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val docCardToUpdate = MkPlcDocCard(
            id = MkPlcDocCardId("123"),
            title = "xyz",
            description = "xyz",
            docCardType = MkPlcDocCardType.PDF,
            visibility = MkPlcDocCardVisibility.VISIBLE_TO_GROUP,
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
        assertEquals(docCardToUpdate.id, ctx.mkPlcDocCardResponse.id)
        assertEquals(docCardToUpdate.title, ctx.mkPlcDocCardResponse.title)
        assertEquals(docCardToUpdate.description, ctx.mkPlcDocCardResponse.description)
        assertEquals(docCardToUpdate.docCardType, ctx.mkPlcDocCardResponse.docCardType)
        assertEquals(docCardToUpdate.visibility, ctx.mkPlcDocCardResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
