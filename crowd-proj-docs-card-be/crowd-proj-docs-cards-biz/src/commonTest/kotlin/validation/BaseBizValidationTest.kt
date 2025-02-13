package validation

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand


abstract class BaseBizValidationTest {
    protected abstract val command: MkPlcDocCardCommand
    private val settings by lazy { MkPlcCorSettings() }
    protected val processor by lazy { MkPlcDocCardProcessor(settings) }
}
