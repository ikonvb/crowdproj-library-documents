package ru.otus.crowd.proj.docs.cards.biz

import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardType


@Suppress("unused")
class MkPlcDocCardProcessor(val corSettings: MkPlcCorSettings) {
    suspend fun exec(ctx: MkPlcDocCardContext) {
        ctx.mkPlcDocCardResponse = MkPlcDocCardStubSingleton.get()
        ctx.mkPlcDocCardsResponse =
            MkPlcDocCardStubSingleton.prepareSearchList("doc card search", MkPlcDocCardType.PDF).toMutableList()
        ctx.state = MkPlcDocCardState.RUNNING
    }
}