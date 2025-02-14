package validation

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import kotlin.test.Test


class BizValidationCreateTest : BaseBizValidationTest() {

    override val command: MkPlcDocCardCommand = MkPlcDocCardCommand.CREATE

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)
    @Test
    fun trimTitle() = validationTitleTrim(command, processor)
    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test
    fun badSymbolsTitle() = validationTitleSymbols(command, processor)
    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test
    fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
}
