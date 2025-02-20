package plugins

import com.benasher44.uuid.uuid4
import config.ConfigPaths
import config.PostgresConfig
import configs.CassandraConfig
import crowd.proj.docs.cards.cassandra.repo.RepoDocCardCassandra
import crowd.proj.docs.cards.common.repo.IRepoDocCard
import crowd.proj.docs.cards.pg.repo.RepoDocCardPostgres
import crowd.proj.docs.cards.pg.repo.SqlProperties
import io.ktor.server.application.*

actual fun Application.getDatabaseConf(type: DocCardDbType): IRepoDocCard {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        "cassandra", "nosql", "cass" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

private fun Application.initCassandra(): IRepoDocCard {
    val config = CassandraConfig(environment.config)
    return RepoDocCardCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}

fun Application.initPostgres(): IRepoDocCard {
    val config = PostgresConfig(environment.config)
    return RepoDocCardPostgres(
        properties = SqlProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
            schema = config.schema,
            database = config.database,
        ),
        randomUuid = { uuid4().toString() },
    )
}