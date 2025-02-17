package ru.otus.crowd.proj.docs.cards.common

import kotlinx.datetime.Instant
import ru.otus.crowd.proj.docs.cards.common.models.*
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

data class MkPlcDocCardContext(
    var docCardValidating: MkPlcDocCard = MkPlcDocCard(),
    var docCardFilterValidating: MkPlcDocCardFilter = MkPlcDocCardFilter(),

    var docCardValidated: MkPlcDocCard = MkPlcDocCard(),
    var docCardFilterValidated: MkPlcDocCardFilter = MkPlcDocCardFilter(),
    var command: MkPlcDocCardCommand = MkPlcDocCardCommand.NONE,
    var corSettings: MkPlcDocCardCorSettings = MkPlcDocCardCorSettings(),
    var state: MkPlcDocCardState = MkPlcDocCardState.NONE,
    val errors: MutableList<MkPlcDocCardError> = mutableListOf(),
    var workMode: MkPlcDocCardWorkMode = MkPlcDocCardWorkMode.TEST,
    var stubCase: MkPlcDocCardStubs = MkPlcDocCardStubs.NONE,
    var requestId: MkPlcDocCardRequestId = MkPlcDocCardRequestId.Companion.NONE,
    var timeStart: Instant = Instant.Companion.NONE,
    var mkPlcDocCardRequest: MkPlcDocCard = MkPlcDocCard(),
    var mkPlcDocCardFilterRequest: MkPlcDocCardFilter = MkPlcDocCardFilter(),
    var mkPlcDocCardResponse: MkPlcDocCard = MkPlcDocCard(),
    var mkPlcDocCardsResponse: MutableList<MkPlcDocCard> = mutableListOf(),

    var docCardRepo: IRepoDocCard = IRepoDocCard.NONE,
    var docCardRepoRead: MkPlcDocCard = MkPlcDocCard(),
    var docCardRepoPrepare: MkPlcDocCard = MkPlcDocCard(),
    var docCardRepoDone: MkPlcDocCard = MkPlcDocCard(),
    var docCardsRepoDone: MutableList<MkPlcDocCard> = mutableListOf(),
)