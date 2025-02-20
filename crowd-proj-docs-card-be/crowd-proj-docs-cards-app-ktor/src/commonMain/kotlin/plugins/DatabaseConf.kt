package plugins

import crowd.proj.docs.cards.common.repo.IRepoDocCard
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import io.ktor.server.application.*
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