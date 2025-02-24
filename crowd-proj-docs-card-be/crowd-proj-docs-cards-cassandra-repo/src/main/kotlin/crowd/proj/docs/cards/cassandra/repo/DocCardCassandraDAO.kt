package crowd.proj.docs.cards.cassandra.repo

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import crowd.proj.docs.cards.cassandra.repo.model.DocCardCassandraDTO
import crowd.proj.docs.cards.common.repo.DbDocCardSearchRequest
import java.util.concurrent.CompletionStage

@Dao
interface DocCardCassandraDAO {

    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: DocCardCassandraDTO): CompletionStage<Unit>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(id: String): CompletionStage<DocCardCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: DocCardCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(
        customWhereClause = "id = :id",
        customIfClause = "lock = :prevLock",
        entityClass = [DocCardCassandraDTO::class]
    )
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @QueryProvider(providerClass = DocCardCassandraSearchProvider::class, entityHelpers = [DocCardCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun search(filter: DbDocCardSearchRequest): CompletionStage<Collection<DocCardCassandraDTO>>
}