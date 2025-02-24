package crowd.proj.docs.cards.tests.repo


import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardOwnerId
import crowd.proj.docs.cards.common.models.MkPlcDocCardType
import crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import crowd.proj.docs.cards.common.repo.DbDocCardsResponseOk
import crowd.proj.docs.cards.common.repo.IDbDocCardsResponse
import crowd.proj.docs.cards.common.repo.IRepoDocCard
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDocCardSearchTest {

    abstract val repo: IRepoDocCard

    protected open val initializedObjects: List<MkPlcDocCard> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchDocCard(DbDocCardSearchRequest(ownerId = searchOwnerId))
        assertIs<DbDocCardsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchDocType() = runRepoTest {

        val result: IDbDocCardsResponse = repo.searchDocCard(DbDocCardSearchRequest(docCardType = MkPlcDocCardType.PNG))

        assertIs<DbDocCardsResponseOk>(result)

        val expected: List<MkPlcDocCard> =
            listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }

        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object : BaseInitDocCards("search") {

        val searchOwnerId = MkPlcDocCardOwnerId("owner-124")

        override val initObjects: List<MkPlcDocCard> = listOf(
            createInitTestModel("docCard1"),
            createInitTestModel("docCard2", ownerId = searchOwnerId),
            createInitTestModel("docCard3", docCardType = MkPlcDocCardType.PNG),
            createInitTestModel("docCard4", ownerId = searchOwnerId),
            createInitTestModel("docCard5", docCardType = MkPlcDocCardType.PNG),
        )
    }
}
