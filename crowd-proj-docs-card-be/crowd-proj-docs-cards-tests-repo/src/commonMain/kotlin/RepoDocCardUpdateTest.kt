package crowd.proj.docs.cards.tests.repo

import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDocCardUpdateTest {
    abstract val repo: IRepoDocCard
    protected open val updateSuccess = initObjects[0]
    protected val updateIdNotFound = MkPlcDocCardId("docCard-repo-update-not-found")

    private val reqUpdateSuccess by lazy {
        MkPlcDocCard(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            ownerId = MkPlcOwnerId("owner-123"),
            visibility = MkPlcVisibility.VISIBLE_TO_GROUP,
            docCardType = MkPlcDocCardType.PDF,
        )
    }
    private val reqUpdateNotFound = MkPlcDocCard(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MkPlcOwnerId("owner-123"),
        visibility = MkPlcVisibility.VISIBLE_TO_GROUP,
        docCardType = MkPlcDocCardType.PDF,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateDocCard(DbDocCardRequest(reqUpdateSuccess))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals(reqUpdateSuccess.id, result.data.id)
        assertEquals(reqUpdateSuccess.title, result.data.title)
        assertEquals(reqUpdateSuccess.description, result.data.description)
        assertEquals(reqUpdateSuccess.docCardType, result.data.docCardType)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateDocCard(DbDocCardRequest(reqUpdateNotFound))
        assertIs<DbDocCardResponseError>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDocCards("update") {
        override val initObjects: List<MkPlcDocCard> = listOf(
            createInitTestModel("update"),
        )
    }
}
