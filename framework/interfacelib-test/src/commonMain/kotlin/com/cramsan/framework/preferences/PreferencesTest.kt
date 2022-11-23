package com.cramsan.framework.preferences

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
abstract class PreferencesTest : TestBase() {

    protected lateinit var preferences: Preferences

    /**
     * Save a string and try to read it.
     */
    @Test
    fun testSaveString() {
        preferences.saveString("Hello1", "Test2")

        val result = preferences.loadString("Hello1")

        assertEquals("Test2", result)
    }

    /**
     * Save an int and try to read it.
     */
    @Test
    fun testSaveInt() {
        preferences.saveInt("Hello1", 3)

        val result = preferences.loadInt("Hello1")

        assertEquals(3, result)
    }

    /**
     * Save a long and try to read it.
     */
    @Test
    fun testSaveLong() {
        preferences.saveLong("Hello1", 2)

        val result = preferences.loadLong("Hello1")

        assertEquals(2, result)
    }

    /**
     * Remove a previously saved key.
     */
    @Test
    fun testRemove() {
        preferences.saveString("Hello1", "Test2")

        preferences.remove("Hello1")

        assertNull(preferences.loadString("Hello1"))
    }
}
