package plugins

import config.ConfigPaths
import crowd.proj.docs.cards.common.repo.IRepoDocCard
import io.ktor.server.application.*
import kotlin.text.lowercase

actual fun Application.getDatabaseConf(type: DocCardDbType): IRepoDocCard {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}
