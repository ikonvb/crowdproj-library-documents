package crowd.proj.docs.cards.tests.repo

import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDocCardUpdateTest {

    abstract val repo: IRepoDocCard
    protected open val updateSuccess = initObjects[0]
    protected open val updateConcurrency = initObjects[1]
    protected val updateIdNotFound = MkPlcDocCardId("docCard-repo-update-not-found")
    protected val lockBad = MkPlcDocCardLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MkPlcDocCardLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSuccess by lazy {
        MkPlcDocCard(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            ownerId = MkPlcDocCardOwnerId("owner-123"),
            visibility = MkPlcDocCardVisibility.VISIBLE_TO_GROUP,
            docCardType = MkPlcDocCardType.PDF,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MkPlcDocCard(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MkPlcDocCardOwnerId("owner-123"),
        visibility = MkPlcDocCardVisibility.VISIBLE_TO_GROUP,
        docCardType = MkPlcDocCardType.PDF,
        lock = initObjects.first().lock,
    )

    private val reqUpdateConcurrency by lazy {
        MkPlcDocCard(
            id = updateConcurrency.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = MkPlcDocCardOwnerId("owner-123"),
            visibility = MkPlcDocCardVisibility.VISIBLE_TO_GROUP,
            docCardType = MkPlcDocCardType.PDF,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateDocCard(DbDocCardUpdateRequest(reqUpdateSuccess))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals(reqUpdateSuccess.id, result.data.id)
        assertEquals(reqUpdateSuccess.title, result.data.title)
        assertEquals(reqUpdateSuccess.description, result.data.description)
        assertEquals(reqUpdateSuccess.docCardType, result.data.docCardType)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateDocCard(DbDocCardUpdateRequest(reqUpdateNotFound))
        assertIs<DbDocCardResponseError>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }


    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateDocCard(DbDocCardUpdateRequest(reqUpdateConcurrency))
        assertIs<DbDocCardResponseErrorWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(reqUpdateConcurrency, result.data)
    }

    companion object : BaseInitDocCards("update") {
        override val initObjects: List<MkPlcDocCard> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConcurrency"),
        )
    }
}
