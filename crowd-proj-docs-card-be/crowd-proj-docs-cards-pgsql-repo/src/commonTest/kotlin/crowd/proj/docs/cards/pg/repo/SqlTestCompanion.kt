package crowd.proj.docs.cards.pg.repo

import com.benasher44.uuid.uuid4
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import crowd.proj.docs.cards.common.models.MkPlcDocCard


object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    private val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<MkPlcDocCard> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IDocCardRepoInitializable = DocCardRepoInitialized(
        repo = RepoDocCardSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

