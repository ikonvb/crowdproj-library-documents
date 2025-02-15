package crowd.proj.docs.cards.inmemory.repo

import com.benasher44.uuid.uuid4
import crowd.proj.docs.cards.common.repo.IDocCardRepoInitializable
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcOwnerId
import ru.otus.crowd.proj.docs.cards.common.repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class DocCardRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : DocCardRepoBase(), IRepoDocCard, IDocCardRepoInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, DocCardEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(docCards: Collection<MkPlcDocCard>) = docCards.map { docCard ->
        val entity = DocCardEntity(docCard)
        require(entity.id != null)
        cache.put(entity.id, entity)
        docCard
    }

    override suspend fun createDocCard(rq: DbDocCardRequest): IDbDocCardResponse = tryDocCardMethod {

        val key = randomUuid()
        val docCard = rq.docCard.copy(id = MkPlcDocCardId(key))
        val entity = DocCardEntity(docCard)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbDocCardResponseOk(docCard)
    }

    override suspend fun readDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse = tryDocCardMethod {
        val key = rq.id.takeIf { it != MkPlcDocCardId.NONE }?.asString() ?: return@tryDocCardMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbDocCardResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateDocCard(rq: DbDocCardRequest): IDbDocCardResponse = tryDocCardMethod {
        val rqDocCard = rq.docCard
        val id = rqDocCard.id.takeIf { it != MkPlcDocCardId.NONE } ?: return@tryDocCardMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldDocCard = cache.get(key)?.toInternal()
            when {
                oldDocCard == null -> errorNotFound(id)
                else -> {
                    val newDocCard = rqDocCard.copy()
                    val entity = DocCardEntity(newDocCard)
                    cache.put(key, entity)
                    DbDocCardResponseOk(newDocCard)
                }
            }
        }
    }


    override suspend fun deleteDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse = tryDocCardMethod {
        val id = rq.id.takeIf { it != MkPlcDocCardId.NONE } ?: return@tryDocCardMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldDocCard = cache.get(key)?.toInternal()
            when {
                oldDocCard == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbDocCardResponseOk(oldDocCard)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchDocCard(rq: DbDocCardFilterRequest): IDbDocCardsResponse = tryDocCardsMethod {

        val result: List<MkPlcDocCard> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MkPlcOwnerId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } != false
            }
            .filter { entry ->
                rq.docCardType.takeIf { it != MkPlcDocCardType.UNKNOWN }?.let {
                    it.name == entry.value.docCardType
                } != false
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) == true
                } != false
            }
            .map { it.value.toInternal() }
            .toList()
        DbDocCardsResponseOk(result)
    }
}