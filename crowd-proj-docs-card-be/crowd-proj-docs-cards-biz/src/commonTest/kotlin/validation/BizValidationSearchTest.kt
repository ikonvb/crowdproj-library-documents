package validation

import kotlinx.coroutines.test.runTest
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardFilter
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest : BaseBizValidationTest() {
    override val command = MkPlcDocCardCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = MkPlcDocCardContext(
            command = command,
            state = MkPlcDocCardState.NONE,
            workMode = MkPlcDocCardWorkMode.TEST,
            mkPlcDocCardFilterRequest = MkPlcDocCardFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MkPlcDocCardState.FAILING, ctx.state)
    }
}
