package validation

import crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import kotlin.test.Test

class BizValidationOffersTest : BaseBizValidationTest() {
    override val command = MkPlcDocCardCommand.OFFERS

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

}
