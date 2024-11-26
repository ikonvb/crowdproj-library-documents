package ru.otus.crowd.proj.docs.cards.common

import kotlinx.datetime.Instant
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardRequestId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardFilter
import ru.otus.crowd.proj.docs.cards.common.stubs.MkPlcDocCardStubs

data class MkPlcDocCardContext(
    var command: MkPlcDocCardCommand = MkPlcDocCardCommand.NONE,
    var state: MkPlcDocCardState = MkPlcDocCardState.NONE,
    val errors: MutableList<MkPlcDocCardError> = mutableListOf(),
    var workMode: MkPlcDocCardWorkMode = MkPlcDocCardWorkMode.TEST,
    var stubCase: MkPlcDocCardStubs = MkPlcDocCardStubs.NONE,
    var requestId: MkPlcDocCardRequestId = MkPlcDocCardRequestId.Companion.NONE,
    var timeStart: Instant = Instant.Companion.NONE,
    var mkPlcDocCardRequest: MkPlcDocCard = MkPlcDocCard(),
    var mkPlcDocCardFilterRequest: MkPlcDocCardFilter = MkPlcDocCardFilter(),
    var mkPlcDocCardResponse: MkPlcDocCard = MkPlcDocCard(),
    var mkPlcDocCardsResponse: MutableList<MkPlcDocCard> = mutableListOf()
    )