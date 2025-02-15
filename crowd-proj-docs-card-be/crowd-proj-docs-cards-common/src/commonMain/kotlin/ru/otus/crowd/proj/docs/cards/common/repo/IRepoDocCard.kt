package ru.otus.crowd.proj.docs.cards.common.repo

interface IRepoDocCard {
    suspend fun createDocCard(rq: DbDocCardRequest): IDbDocCardResponse
    suspend fun readDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse
    suspend fun updateDocCard(rq: DbDocCardRequest): IDbDocCardResponse
    suspend fun deleteDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse
    suspend fun searchDocCard(rq: DbDocCardFilterRequest): IDbDocCardsResponse

    companion object {

        val NONE = object : IRepoDocCard {
            override suspend fun createDocCard(rq: DbDocCardRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateDocCard(rq: DbDocCardRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteDocCard(rq: DbDocCardIdRequest): IDbDocCardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchDocCard(rq: DbDocCardFilterRequest): IDbDocCardsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
