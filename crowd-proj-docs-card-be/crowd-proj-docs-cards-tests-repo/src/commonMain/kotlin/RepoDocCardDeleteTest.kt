package crowd.proj.docs.cards.tests.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoDocCardDeleteTest {
    abstract val repo: IRepoDocCard
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = MkPlcDocCardId("docCard-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteDocCard(DbDocCardDeleteRequest(deleteSuccess.id, lock = lockOld))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals(deleteSuccess.title, result.data.title)
        assertEquals(deleteSuccess.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {

        val result = repo.readDocCard(DbDocCardReadRequest(notFoundId, lock = lockOld))

        assertIs<DbDocCardResponseError>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {

        val result = repo.deleteDocCard(DbDocCardDeleteRequest(deleteConc.id, lock = lockBad))

        assertIs<DbDocCardResponseErrorWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitDocCards("delete") {
        override val initObjects: List<MkPlcDocCard> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
