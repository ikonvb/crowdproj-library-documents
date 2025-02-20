package plugins

import config.ConfigPaths
import configs.CassandraConfig
import crowd.proj.docs.cards.cassandra.repo.RepoDocCardCassandra
import crowd.proj.docs.cards.common.repo.IRepoDocCard
import io.ktor.server.application.*

actual fun Application.getDatabaseConf(type: DocCardDbType): IRepoDocCard {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
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