package plugins

import config.PostgresConfig
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import crowd.proj.docs.cards.pg.repo.RepoDocCardSql
import crowd.proj.docs.cards.pg.repo.SqlProperties
import io.ktor.server.application.*
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

expect fun Application.getDatabaseConf(type: DocCardDbType): IRepoDocCard

enum class DocCardDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.initInMemory(): IRepoDocCard {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return DocCardRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}


fun Application.initPostgres(): IRepoDocCard {
    val config = PostgresConfig(environment.config)
    return RepoDocCardSql(
        properties = SqlProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
            schema = config.schema,
            database = config.database,
        )
    )
}
