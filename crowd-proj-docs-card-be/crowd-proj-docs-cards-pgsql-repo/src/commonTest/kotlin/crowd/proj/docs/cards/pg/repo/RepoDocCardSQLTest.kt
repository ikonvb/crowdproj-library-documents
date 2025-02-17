package crowd.proj.docs.cards.pg.repo


import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import crowd.proj.docs.cards.tests.repo.*
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard
import kotlin.test.AfterTest

private fun IRepoDocCard.clear() {
    val pgRepo = (this as DocCardRepoInitialized).repo as RepoDocCardSql
    pgRepo.clear()
}

class RepoDocCardSQLCreateTest : RepoDocCardCreateTest() {

    override val repo: IDocCardRepoInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoDocCardSQLReadTest : RepoDocCardReadTest() {
    override val repo: IRepoDocCard = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoDocCardSQLUpdateTest : RepoDocCardUpdateTest() {

    override val repo: IRepoDocCard = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )

    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoDocCardSQLDeleteTest : RepoDocCardDeleteTest() {

    override val repo: IRepoDocCard = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoDocCardSQLSearchTest : RepoDocCardSearchTest() {

    override val repo: IRepoDocCard = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}
