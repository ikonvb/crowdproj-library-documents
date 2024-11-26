package ru.otus.crowd.proj.docs.cards.common.models

data class MkPlcDocCardError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
