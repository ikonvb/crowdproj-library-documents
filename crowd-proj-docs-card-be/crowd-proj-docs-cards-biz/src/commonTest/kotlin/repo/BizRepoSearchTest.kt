package repo

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.*
import crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = MkPlcDocCardOwnerId("321")
    private val command = MkPlcDocCardCommand.SEARCH
    private val initDocCard = MkPlcDocCard(
        id = MkPlcDocCardId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        docCardType = MkPlcDocCardType.PDF,
        visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
    )

    private val repo = DocCardRepositoryMock(
        invokeSearchDocCard = {
            DbDocCardsResponseOk(
                data = listOf(initDocCard),
            )
        }
    )
    private val settings = MkPlcDocCardCorSettings(repoTest = repo)
    private val processor = MkPlcDocCardProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = MkPlcDocCardContext(
            command = command,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.TEST,
            mkPlcDocCardFilterRequest = MkPlcDocCardFilter(
                searchString = "abc",
                docCardType = MkPlcDocCardType.PDF,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkPlcDocCardState.FINISHING, ctx.state)
        assertEquals(1, ctx.mkPlcDocCardsResponse.size)
    }
}
