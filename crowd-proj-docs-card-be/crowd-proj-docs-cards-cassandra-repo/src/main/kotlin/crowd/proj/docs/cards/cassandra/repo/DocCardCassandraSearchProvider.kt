package crowd.proj.docs.cards.cassandra.repo

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import crowd.proj.docs.cards.cassandra.repo.model.DocCardCassandraDTO
import crowd.proj.docs.cards.cassandra.repo.model.toTransport
import crowd.proj.docs.cards.common.models.MkPlcDocCardOwnerId
import crowd.proj.docs.cards.common.models.MkPlcDocCardType
import crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class DocCardCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<DocCardCassandraDTO>
) {
    fun search(filter: DbDocCardSearchRequest): CompletionStage<Collection<DocCardCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.titleFilter.isNotBlank()) {
            select = select
                .whereColumn(DocCardCassandraDTO.COLUMN_TITLE)
                .like(QueryBuilder.literal("%${filter.titleFilter}%"))
        }
        if (filter.ownerId != MkPlcDocCardOwnerId.NONE) {
            select = select
                .whereColumn(DocCardCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.docCardType != MkPlcDocCardType.UNKNOWN) {
            select = select
                .whereColumn(DocCardCassandraDTO.COLUMN_DOC_CARD_TYPE)
                .isEqualTo(
                    QueryBuilder.literal(
                        filter.docCardType.toTransport(),
                        context.session.context.codecRegistry
                    )
                )
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<DocCardCassandraDTO>()
        private val future = CompletableFuture<Collection<DocCardCassandraDTO>>()
        val stage: CompletionStage<Collection<DocCardCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}