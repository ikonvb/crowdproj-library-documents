package crowd.proj.docs.cards.pg.repo

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val DOC_TYPE = "doc_type"
    const val VISIBILITY = "visibility"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"
    const val PRODUCT_ID = "product_id"

    const val DOC_TYPE_TYPE = "doc_types_type"
    const val DOC_TYPE_PDF = "pdf"
    const val DOC_TYPE_PNG = "png"
    const val DOC_TYPE_JPEG = "jpeg"
    const val DOC_TYPE_MS_WORD = "ms_word"

    const val VISIBILITY_TYPE = "doc_visibilities_type"
    const val VISIBILITY_PUBLIC = "public"
    const val VISIBILITY_OWNER = "owner"
    const val VISIBILITY_GROUP = "group"

    const val FILTER_TITLE = TITLE
    const val FILTER_OWNER_ID = OWNER_ID
    const val FILTER_DOC_TYPE = DOC_TYPE

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, DOC_TYPE, VISIBILITY, LOCK, OWNER_ID, PRODUCT_ID,
    )
}