package ru.otus.crowd.proj.docs.cards.common.repo

interface IRepoDocCard {

    suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse
    suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse
    suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse
    suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse
    suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse

    companion object {

        val NONE = object : IRepoDocCard {
            override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
