package crowd.proj.docs.cards.pg.repo

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.test.runTest
import platform.posix.getenv
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardCreateRequest
import ru.otus.crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import kotlin.test.Test

class RepoDocCardSqlTest {
    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun create() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoDocCardSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.createDocCard(
            rq = DbDocCardCreateRequest(
                MkPlcDocCard(
                    title = "tttt",
                    description = "zzzz",
                    visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
                    docCardType = MkPlcDocCardType.PDF,
                    ownerId = MkPlcDocCardOwnerId("1234"),
                    productId = MkPlcDocCardProductId("4567"),
                    lock = MkPlcDocCardLock("235356"),
                )
            )
        )
        println("CREATED $res")
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun search() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoDocCardSql(
            properties = SqlProperties(port = pgPort)
        )

        val res = repo.searchDocCard(
            rq = DbDocCardSearchRequest(
                titleFilter = "tttt",
                docCardType = MkPlcDocCardType.PDF,
                ownerId = MkPlcDocCardOwnerId("1234"),
            )
        )
        println("SEARCH $res")
    }
}
