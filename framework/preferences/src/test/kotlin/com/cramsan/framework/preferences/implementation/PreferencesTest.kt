package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.test.TestBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @Author cramsan
 * @created 1/16/2021
 */
@ExperimentalCoroutinesApi
class PreferencesTest : TestBase() {

    lateinit var preferences: Preferences

    override fun setupTest() {
        val preferencesDelegate = MockPreferencesDelegate()
        preferences = PreferencesImpl(preferencesDelegate)
    }

    @Test
    fun testSaveString() {
        assertNull(preferences.loadString("Hello1"))
        preferences.saveString("Hello1", "Test2")
        assertEquals("Test2", preferences.loadString("Hello1"))
        preferences.saveString("Hello1", null)
        assertNull(preferences.loadString("Hello1"))
    }

    @Test
    fun testSaveInt() {
        assertNull(preferences.loadInt("Hello2"))
        preferences.saveInt("Hello2", 123)
        assertEquals(123, preferences.loadInt("Hello2"))
        preferences.saveInt("Hello2", null)
        assertNull(preferences.loadInt("Hello1"))
    }

    @Test
    fun testSaveLong() {
        assertNull(preferences.loadLong("Alpha2"))
        preferences.saveLong("Alpha2", 555)
        assertEquals(555, preferences.loadLong("Alpha2"))
        preferences.saveLong("Alpha2", null)
        assertNull(preferences.loadLong("Alpha2"))
    }
}
