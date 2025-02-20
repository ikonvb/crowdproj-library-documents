package crowd.proj.docs.cards.pg.repo


import com.benasher44.uuid.uuid4
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import crowd.proj.docs.cards.tests.repo.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration


@RunWith(Enclosed::class)
class PostgresTest {

    class RepoDocCardSQLCreateTest : RepoDocCardCreateTest() {

        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository(uuid = lockNew.asString())
        )
    }

    class RepoDocCardSQLReadTest : RepoDocCardReadTest() {
        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    class RepoDocCardSQLUpdateTest : RepoDocCardUpdateTest() {

        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = initObjects,
            repo = repository(uuid = lockNew.asString())
        )
    }

    class RepoDocCardSQLDeleteTest : RepoDocCardDeleteTest() {

        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = RepoDocCardReadTest.Companion.initObjects,
            repo = repository()
        )
    }

    class RepoDocCardSQLSearchTest : RepoDocCardSearchTest() {

        override val repo: IDocCardRepoInitializable = DocCardRepoInitialized(
            initObjects = RepoDocCardReadTest.Companion.initObjects,
            repo = repository()
        )
    }


    @Ignore
    companion object {

        class CustomPostgresContainer(image: String) : PostgreSQLContainer<CustomPostgresContainer>(image)

        private val container by lazy {
            CustomPostgresContainer("postgres:13").withStartupTimeout(Duration.ofSeconds(300L))
        }

        fun repository(uuid: String? = null): RepoDocCardPostgres {
            return RepoDocCardPostgres(
                properties = SqlProperties(
                    host = container.host,
                    port = container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                ),
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