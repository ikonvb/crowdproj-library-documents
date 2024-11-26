package ru.otus.crowd.proj.docs.cards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkPlcOwnerId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MkPlcOwnerId("")
    }
}
