package ru.otus.crowd.proj.docs.api.v2.mappers

import crowd.proj.docs.cards.common.models.MkPlcDocCard
import crowd.proj.docs.cards.common.models.MkPlcDocCardId
import crowd.proj.docs.cards.common.models.MkPlcDocCardLock
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardCreateObject
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardDeleteObject
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardReadObject
import ru.otus.crowd.proj.docs.be.api.v2.models.DocCardUpdateObject


fun MkPlcDocCard.toTransportCreate() = DocCardCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    docType = docCardType.toTransportDocCard(),
    visibility = visibility.toTransportDocCard(),
)

fun MkPlcDocCard.toTransportRead() = DocCardReadObject(
    id = id.takeIf { it != MkPlcDocCardId.NONE }?.asString(),
)

fun MkPlcDocCard.toTransportUpdate() = DocCardUpdateObject(
    id = id.takeIf { it != MkPlcDocCardId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    docType = docCardType.toTransportDocCard(),
    visibility = visibility.toTransportDocCard(),
    lock = lock.takeIf { it != MkPlcDocCardLock.NONE }?.asString(),
)

fun MkPlcDocCard.toTransportDelete() = DocCardDeleteObject(
    id = id.takeIf { it != MkPlcDocCardId.NONE }?.asString(),
    lock = lock.takeIf { it != MkPlcDocCardLock.NONE }?.asString(),
)
