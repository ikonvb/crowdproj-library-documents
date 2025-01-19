package stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class DocCardApiKmpTest {

    @Test
    fun testAddition() {
        val result = 2 + 3
        assertEquals(5, result, "2 + 3 should equal 5")
    }

    @Test
    fun testStringContains() {
        val string = "Kotlin Native"
        assertTrue(string.contains("Native"), "String should contain 'Native'")
    }
}