import crowd.proj.docs.cards.tests.repo.DocCardRepositoryMock
import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DocCardRepositoryMockTest {

    private val repo = DocCardRepositoryMock(
        invokeCreateDocCard = { DbDocCardResponseOk(MkPlcDocCardStubSingleton.prepareResult { title = "create" }) },
        invokeReadDocCard = { DbDocCardResponseOk(MkPlcDocCardStubSingleton.prepareResult { title = "read" }) },
        invokeUpdateDocCard = { DbDocCardResponseOk(MkPlcDocCardStubSingleton.prepareResult { title = "update" }) },
        invokeDeleteDocCard = { DbDocCardResponseOk(MkPlcDocCardStubSingleton.prepareResult { title = "delete" }) },
        invokeSearchDocCard = {
            DbDocCardsResponseOk(listOf(MkPlcDocCardStubSingleton.prepareResult {
                title = "search"
            }))
        },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createDocCard(DbDocCardRequest(MkPlcDocCard()))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readDocCard(DbDocCardIdRequest(MkPlcDocCard()))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateDocCard(DbDocCardRequest(MkPlcDocCard()))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteDocCard(DbDocCardIdRequest(MkPlcDocCard()))
        assertIs<DbDocCardResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchDocCard(DbDocCardFilterRequest())
        assertIs<DbDocCardsResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }

}
