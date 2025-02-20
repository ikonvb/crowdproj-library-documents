package crowd.proj.docs.cards.pg.repo

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "marketplace-pass",
    val database: String = "mk_plc_doc_cards",
    val schema: String = "public",
    val table: String = "doc_cards",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
