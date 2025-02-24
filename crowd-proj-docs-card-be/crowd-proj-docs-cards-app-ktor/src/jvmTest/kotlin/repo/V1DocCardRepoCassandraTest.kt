package repo

import com.benasher44.uuid.uuid4
import config.MkPlcAppSettings
import crowd.proj.docs.cards.cassandra.repo.RepoDocCardCassandra
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.common.repo.IRepoDocCard
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.utility.MountableFile
import ru.otus.crowd.proj.docs.be.api.v1.models.DocCardRequestDebugMode
import java.time.Duration

class V1DocCardRepoCassandraTest : V1DocCardRepoBaseTest() {
    override val workMode: DocCardRequestDebugMode = DocCardRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoDocCard) = MkPlcAppSettings(
        corSettings = MkPlcDocCardCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(repository("ks_create", uuidNew))
    )
    override val appSettingsRead: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            repository("ks_read"),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsUpdate: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            repository("ks_update", uuidNew),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsDelete: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            repository("ks_delete"),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsSearch: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            repository("ks_search"),
            initObjects = listOf(initDocCard),
        )
    )
    override val appSettingsOffers: MkPlcAppSettings = mkAppSettings(
        repo = DocCardRepoInitialized(
            repository("ks_offers"),
            initObjects = listOf(initDocCard, initDocCardImg),
        )
    )

    companion object {
        class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:5.0.2") {
            init {
                withStartupTimeout(Duration.ofSeconds(300L))
                withCopyFileToContainer(
                    MountableFile.forClasspathResource("cassandra.yaml"), // From `src/test/resources`
                    "/etc/cassandra/cassandra.yaml" // Destination in container
                )

            }
        }

        private val container by lazy {
            @Suppress("Since15")
            TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
        }

        @JvmStatic
        @BeforeClass
        fun tearUp() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            container.stop()
        }

        fun repository(keyspace: String, uuid: String? = null): RepoDocCardCassandra {
            return RepoDocCardCassandra(
                keyspaceName = keyspace,
                host = container.host,
                port = container.getMappedPort(CassandraContainer.CQL_PORT),
                randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
            )
        }
    }
}
