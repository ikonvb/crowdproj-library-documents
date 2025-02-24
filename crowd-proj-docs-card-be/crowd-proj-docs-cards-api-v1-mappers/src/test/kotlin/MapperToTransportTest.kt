package ru.otus.otuskotlin.marketplace.mappers.v1

import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.exceptions.UnknownMkPlcCommand
import crowd.proj.docs.cards.common.models.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.otus.crowd.proj.docs.be.api.v1.models.*
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.*

class MapperToTransportTest : FunSpec({

    context("toTransport") {

        test("should map from MkPlcDocCardContext to DocCardCreateResponse correctly") {
            val context = MkPlcDocCardContext(
                requestId = MkPlcDocCardRequestId("1234"),
                command = MkPlcDocCardCommand.CREATE,
                mkPlcDocCardResponse = MkPlcDocCard(
                    title = "title",
                    description = "desc",
                    visibility = MkPlcDocCardVisibility.VISIBLE_PUBLIC,
                ),
                errors = mutableListOf(
                    MkPlcDocCardError(
                        code = "err",
                        group = "request",
                        field = "title",
                        message = "wrong title",
                    )
                ),
                state = MkPlcDocCardState.RUNNING,
            )

            val req = context.toTransportDocCard() as DocCardCreateResponse

            req.docCard?.title shouldBe "title"
            req.docCard?.description shouldBe "desc"
            req.docCard?.visibility shouldBe DocCardVisibility.PUBLIC
            req.errors?.size shouldBe 1
            req.errors?.firstOrNull()?.code shouldBe "err"
            req.errors?.firstOrNull()?.group shouldBe "request"
            req.errors?.firstOrNull()?.field shouldBe "title"
            req.errors?.firstOrNull()?.message shouldBe "wrong title"
        }
    }

    val context = MkPlcDocCardContext(
        command = MkPlcDocCardCommand.CREATE,
        state = MkPlcDocCardState.RUNNING,
        errors = mutableListOf(MkPlcDocCardError("err", "group", "field", "message")),
        mkPlcDocCardResponse = MkPlcDocCard(title = "Test title", description = "Test description"),
        mkPlcDocCardsResponse = mutableListOf(MkPlcDocCard(title = "Test title", description = "Test description"))
    )

    context("toTransportDocCard") {

        test("should return DocCardCreateResponse for CREATE command") {

            val response = context.toTransportDocCard()
            response shouldBe DocCardCreateResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description", ownerId = null)
            )
        }

        test("should return DocCardReadResponse for READ command") {
            context.command = MkPlcDocCardCommand.READ
            val response = context.toTransportDocCard()
            response shouldBe DocCardReadResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }

        test("should return DocCardUpdateResponse for UPDATE command") {
            context.command = MkPlcDocCardCommand.UPDATE
            val response = context.toTransportDocCard()
            response shouldBe DocCardUpdateResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }

        test("should return DocCardDeleteResponse for DELETE command") {
            context.command = MkPlcDocCardCommand.DELETE
            val response = context.toTransportDocCard()
            response shouldBe DocCardDeleteResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }

        test("should return DocCardSearchResponse for SEARCH command") {
            context.command = MkPlcDocCardCommand.SEARCH
            val response = context.toTransportDocCard()
            response shouldBe DocCardSearchResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCards = listOf(DocCardResponseObject(title = "Test title", description = "Test description"))
            )
        }

        test("should return DocCardOffersResponse for OFFERS command") {
            context.command = MkPlcDocCardCommand.OFFERS

            val response = context.toTransportDocCard()

            response shouldBe DocCardOffersResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description"),
                docCards = listOf(DocCardResponseObject(title = "Test title", description = "Test description"))
            )
        }

        test("should throw UnknownMkPlcCommand for NONE command") {
            context.command = MkPlcDocCardCommand.NONE
            shouldThrow<UnknownMkPlcCommand> {
                context.toTransportDocCard()
            }
        }
    }

    context("toTransportCreate") {
        test("should return DocCardCreateResponse") {
            val response = context.toTransportCreate()
            response shouldBe DocCardCreateResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }
    }

    context("toTransportRead") {
        test("should return DocCardReadResponse") {
            context.command = MkPlcDocCardCommand.READ
            val response = context.toTransportRead()
            response shouldBe DocCardReadResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }
    }

    context("toTransportUpdate") {
        test("should return DocCardUpdateResponse") {
            context.command = MkPlcDocCardCommand.UPDATE
            val response = context.toTransportUpdate()
            response shouldBe DocCardUpdateResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }
    }

    context("toTransportDelete") {
        test("should return DocCardDeleteResponse") {
            context.command = MkPlcDocCardCommand.DELETE
            val response = context.toTransportDelete()
            response shouldBe DocCardDeleteResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description")
            )
        }
    }

    context("toTransportSearch") {
        test("should return DocCardSearchResponse") {
            context.command = MkPlcDocCardCommand.SEARCH
            val response = context.toTransportSearch()
            response shouldBe DocCardSearchResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCards = listOf(DocCardResponseObject(title = "Test title", description = "Test description"))
            )
        }
    }

    context("toTransportOffers") {
        test("should return DocCardOffersResponse") {
            context.command = MkPlcDocCardCommand.OFFERS
            val response = context.toTransportOffers()
            response shouldBe DocCardOffersResponse(
                result = ResponseResult.SUCCESS,
                errors = listOf(Error("err", "group", "field", "message")),
                docCard = DocCardResponseObject(title = "Test title", description = "Test description"),
                docCards = listOf(DocCardResponseObject(title = "Test title", description = "Test description"))
            )
        }
    }

})