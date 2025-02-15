package crowd.proj.docs.cards.tests.repo


import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcOwnerId
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardFilterRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDocCardSearchTest {
    abstract val repo: IRepoDocCard

    protected open val initializedObjects: List<MkPlcDocCard> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchDocCard(DbDocCardFilterRequest(ownerId = searchOwnerId))
        assertIs<DbDocCardsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchDealSide() = runRepoTest {
        val result = repo.searchDocCard(DbDocCardFilterRequest(docCardType = MkPlcDocCardType.PDF))
        assertIs<DbDocCardsResponseOk>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object : BaseInitDocCards("search") {

        val searchOwnerId = MkPlcOwnerId("owner-124")
        override val initObjects: List<MkPlcDocCard> = listOf(
            createInitTestModel("docCard1"),
            createInitTestModel("docCard2", ownerId = searchOwnerId),
            createInitTestModel("docCard3", docCardType = MkPlcDocCardType.PDF),
            createInitTestModel("docCard4", ownerId = searchOwnerId),
            createInitTestModel("docCard5", docCardType = MkPlcDocCardType.PDF),
        )
    }
}
