package ru.otus.crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError

interface IRepoDocCard {

    suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse
    suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse
    suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse
    suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse
    suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse

    companion object {

        val NONE = object : IRepoDocCard {
            override suspend fun createDocCard(rq: DbDocCardCreateRequest): IDbDocCardResponse {
                return DbDocCardResponseError(
                    MkPlcDocCardError(
                        code = "err",
                        group = "request",
                        message = "Not implemented",
                    )
                )
            }

            override suspend fun readDocCard(rq: DbDocCardReadRequest): IDbDocCardResponse {
                return DbDocCardResponseError(
                    MkPlcDocCardError(
                        code = "err",
                        group = "request",
                        message = "Not implemented",
                    )
                )
            }

            override suspend fun updateDocCard(rq: DbDocCardUpdateRequest): IDbDocCardResponse {
                return DbDocCardResponseError(
                    MkPlcDocCardError(
                        code = "err",
                        group = "request",
                        message = "Not implemented",
                    )
                )
            }

            override suspend fun deleteDocCard(rq: DbDocCardDeleteRequest): IDbDocCardResponse {
                return DbDocCardResponseError(
                    MkPlcDocCardError(
                        code = "err",
                        group = "request",
                        message = "Not implemented",
                    )
                )
            }

            override suspend fun searchDocCard(rq: DbDocCardSearchRequest): IDbDocCardsResponse {
                return DbDocCardsResponseError(
                    MkPlcDocCardError(
                        code = "err",
                        group = "request",
                        message = "Not implemented",
                    )
                )
            }
        }
    }
}
