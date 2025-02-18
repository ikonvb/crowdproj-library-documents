package validation

import kotlinx.coroutines.test.runTest
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import crowd.proj.docs.cards.common.models.MkPlcDocCardFilter
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode
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
