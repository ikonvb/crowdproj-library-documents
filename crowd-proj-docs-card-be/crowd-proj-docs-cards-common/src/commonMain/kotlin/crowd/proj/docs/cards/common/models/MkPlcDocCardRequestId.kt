package crowd.proj.docs.cards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkPlcDocCardRequestId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = MkPlcDocCardRequestId("")
    }
}
