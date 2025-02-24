package crowd.proj.docs.cards.cassandra.repo

import com.benasher44.uuid.uuid4
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import crowd.proj.docs.cards.tests.repo.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.CassandraContainer
import java.time.Duration

@RunWith(Enclosed::class)
class CassandraTest {

    class RepoDocCardCassandraCreateTest : RepoDocCardCreateTest() {
        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository("ks_create", uuidNew.asString())
        )
    }

    class RepoDocCardCassandraReadTest : RepoDocCardReadTest() {
        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository("ks_read")
        )
    }

    class RepoDocCardCassandraUpdateTest : RepoDocCardUpdateTest() {
        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository("ks_update", lockNew.asString())
        )
    }

    class RepoDocCardCassandraDeleteTest : RepoDocCardDeleteTest() {
        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository("ks_delete")
        )
    }

    class RepoDocCardCassandraSearchTest : RepoDocCardSearchTest() {
        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository("ks_search")
        )
    }

    @Ignore
    companion object {
        class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

        private val container by lazy {
            TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
        }

        fun repository(keyspace: String, uuid: String? = null): RepoDocCardCassandra {
            return RepoDocCardCassandra(
                keyspaceName = keyspace,
                host = container.host,
                port = container.getMappedPort(CassandraContainer.CQL_PORT),
                randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
            )
        }

        @JvmStatic
        @BeforeClass
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}
