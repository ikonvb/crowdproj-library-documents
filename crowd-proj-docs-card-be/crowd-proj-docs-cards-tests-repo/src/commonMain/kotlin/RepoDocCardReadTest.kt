package crowd.proj.docs.cards.tests.repo

import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardId
import crowd.proj.docs.cards.common.repo.DbDocCardReadRequest
import crowd.proj.docs.cards.common.repo.DbDocCardResponseError
import crowd.proj.docs.cards.common.repo.DbDocCardResponseOk
import crowd.proj.docs.cards.common.repo.IRepoDocCard
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDocCardReadTest {
    abstract val repo: IRepoDocCard
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readDocCard(DbDocCardReadRequest(readSuccess.id))

        assertIs<DbDocCardResponseOk>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readDocCard(DbDocCardReadRequest(notFoundId))

        assertIs<DbDocCardResponseError>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDocCards("delete") {
        override val initObjects: List<MkPlcDocCard> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MkPlcDocCardId("docCard-repo-read-notFound")

    }
}
