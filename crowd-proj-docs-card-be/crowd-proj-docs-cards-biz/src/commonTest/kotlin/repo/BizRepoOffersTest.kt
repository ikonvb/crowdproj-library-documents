package repo

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoOffersTest {

    private val userId = MkPlcOwnerId("321")
    private val command = MkPlcDocCardCommand.OFFERS
    private val initDocCard = MkPlcDocCard(
        id = MkPlcDocCardId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        docCardType = MkPlcDocCardType.PDF,
        visibility = MkPlcVisibility.VISIBLE_PUBLIC,
    )
    private val offerDocCard = MkPlcDocCard(
        id = MkPlcDocCardId("321"),
        title = "abcd",
        description = "xyz",
        docCardType = MkPlcDocCardType.PDF,
        visibility = MkPlcVisibility.VISIBLE_PUBLIC,
    )
    private val repo = DocCardRepositoryMock(
        invokeReadDocCard = {
            DbDocCardResponseOk(
                data = this@BizRepoOffersTest.initDocCard
            )
        },
        invokeSearchDocCard = {
            DbDocCardsResponseOk(
                data = listOf(offerDocCard)
            )
        }
    )

    private val settings = MkPlcCorSettings(repoTest = repo)
    private val processor = MkPlcDocCardProcessor(settings)

    @Test
    fun repoOffersSuccessTest() = runTest {
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
        assertEquals(1, ctx.mkPlcDocCardsResponse.size)
        assertEquals(MkPlcDocCardType.PDF, ctx.mkPlcDocCardsResponse.first().docCardType)
    }

    @Test
    fun repoOffersNotFoundTest() = repoNotFoundTest(command)
}
