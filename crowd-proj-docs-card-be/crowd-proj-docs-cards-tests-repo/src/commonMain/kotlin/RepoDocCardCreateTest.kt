package crowd.proj.docs.cards.tests.repo


import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals


abstract class RepoDocCardCreateTest {

    abstract val repo: IDocCardRepoInitializable
    protected open val uuidNew = MkPlcDocCardId("10000000-0000-0000-0000-000000000001")

    private val createObj = MkPlcDocCard(
        title = "create object",
        description = "create object description",
        ownerId = MkPlcOwnerId("owner-123"),
        visibility = MkPlcVisibility.VISIBLE_TO_GROUP,
        docCardType = MkPlcDocCardType.PDF,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createDocCard(DbDocCardRequest(createObj))
        val expected = createObj
        assertIs<DbDocCardResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.docCardType, result.data.docCardType)
        assertNotEquals(MkPlcDocCardId.NONE, result.data.id)
    }

    companion object : BaseInitDocCards("create") {
        override val initObjects: List<MkPlcDocCard> = emptyList()
    }
}
