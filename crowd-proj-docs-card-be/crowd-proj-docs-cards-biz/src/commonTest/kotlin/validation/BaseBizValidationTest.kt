package validation

import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import ru.otus.crowd.proj.docs.cards.be.stubs.MkPlcDocCardStubSingleton
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand


abstract class BaseBizValidationTest {
    protected abstract val command: MkPlcDocCardCommand
    private val repo = DocCardRepoInitialized(
        repo = DocCardRepoInMemory(),
        initObjects = listOf(
            MkPlcDocCardStubSingleton.get(),
        ),
    )
    private val settings by lazy { MkPlcDocCardCorSettings() }
    protected val processor by lazy { MkPlcDocCardProcessor(settings) }
}