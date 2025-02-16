package ru.otus.otuskotlin.marketplace.mappers.v1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import ru.otus.crowd.proj.docs.be.api.v1.models.*
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.fromTransport
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardVisibility
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.exceptions.UnknownRequestError
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

class MapperFromTransportTest : FunSpec({

    context("fromTransport") {

        test("should map from DocCardCreateRequest to MkPlcDocCardContext correctly") {

            val req = DocCardCreateRequest(
                debug = DocCardDebug(
                    mode = DocCardRequestDebugMode.STUB,
                    stub = DocCardRequestDebugStubs.SUCCESS,
                ),
                docCard = DocCardCreateObject(
                    title = "title",
                    description = "desc",
                    visibility = DocCardVisibility.PUBLIC,
                ),
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(req)

            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.mkPlcDocCardRequest.title shouldBe "title"
            context.mkPlcDocCardRequest.visibility shouldBe MkPlcDocCardVisibility.VISIBLE_PUBLIC
        }
    }

    val debug = DocCardDebug(
        mode = DocCardRequestDebugMode.STUB,
        stub = DocCardRequestDebugStubs.SUCCESS
    )

    context("fromTransport IRequest") {

        test("should handle DocCardCreateRequest") {
            val request = DocCardCreateRequest(
                debug = debug,
                docCard = DocCardCreateObject(
                    title = "title",
                    description = "desc",
                    visibility = DocCardVisibility.PUBLIC
                )
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(request)

            context.command shouldBe MkPlcDocCardCommand.CREATE
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.mkPlcDocCardRequest.title shouldBe "title"
            context.mkPlcDocCardRequest.visibility shouldBe MkPlcDocCardVisibility.VISIBLE_PUBLIC
        }

        test("should handle DocCardReadRequest") {
            val request = DocCardReadRequest(
                debug = debug,
                docCard = DocCardReadObject(id = "123")
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(request)

            context.command shouldBe MkPlcDocCardCommand.READ
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.mkPlcDocCardRequest.id shouldBe MkPlcDocCardId("123")
        }

        test("should handle DocCardUpdateRequest") {
            val request = DocCardUpdateRequest(
                debug = debug,
                docCard = DocCardUpdateObject(
                    id = "123",
                    title = "updated title",
                    description = "updated desc"
                )
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(request)

            context.command shouldBe MkPlcDocCardCommand.UPDATE
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.mkPlcDocCardRequest.id shouldBe MkPlcDocCardId("123")
            context.mkPlcDocCardRequest.title shouldBe "updated title"
        }

        test("should handle DocCardDeleteRequest") {
            val request = DocCardDeleteRequest(
                debug = debug,
                docCard = DocCardDeleteObject(id = "123")
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(request)

            context.command shouldBe MkPlcDocCardCommand.DELETE
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.mkPlcDocCardRequest.id shouldBe MkPlcDocCardId("123")
        }

        test("should handle DocCardSearchRequest") {
            val request = DocCardSearchRequest(
                debug = debug,
                docCardFilter = DocCardSearchFilter(searchString = "search title")
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(request)

            context.command shouldBe MkPlcDocCardCommand.SEARCH
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.mkPlcDocCardFilterRequest.searchString shouldBe "search title"
        }

        test("should handle DocCardOffersRequest") {
            val request = DocCardOffersRequest(
                debug = debug,
                docCardOffers = DocCardReadObject(id = "offer-id")
            )

            val context = MkPlcDocCardContext()
            context.fromTransport(request)

            context.command shouldBe MkPlcDocCardCommand.OFFERS
            context.workMode shouldBe MkPlcDocCardWorkMode.STUB
            context.stubCase shouldBe MkPlcDocCardStubs.SUCCESS
            context.mkPlcDocCardRequest.id shouldBe MkPlcDocCardId("offer-id")
        }

        test("should throw error for unknown IRequest") {

            val unknownRequest: IRequest = object : IRequest {
                override val requestType = "unknown"
            }

            val context = MkPlcDocCardContext()

            val exception = shouldThrow<UnknownRequestError> {
                context.fromTransport(unknownRequest)
            }

            exception.message shouldBe "Class ${unknownRequest.javaClass} cannot be mapped to DocCardContext"
        }
    }

})