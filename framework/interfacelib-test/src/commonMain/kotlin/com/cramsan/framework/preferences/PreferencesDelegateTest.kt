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
abstract class PreferencesDelegateTest : TestBase() {

    protected lateinit var preferencesDelegate: PreferencesDelegate

    /**
     * Test fetching a value without having added a key first.
     */
    @Test
    fun testInitialState() {
        assertNull(preferencesDelegate.loadString("Hello1"))
    }

    /**
     * Save a string and try to read it.
     */
    @Test
    fun testSaveLoadString() {
        preferencesDelegate.saveString("Hello1", "Test2")

        val result = preferencesDelegate.loadString("Hello1")

        assertEquals("Test2", result)
    }

    /**
     * Save an int and try to read it.
     */
    @Test
    fun testSaveLoadInt() {
        preferencesDelegate.saveInt("Hello1", 2)

        val result = preferencesDelegate.loadInt("Hello1")

        assertEquals(2, result)
    }

    /**
     * Save a long and try to read it.
     */
    @Test
    fun testSaveLoadLong() {
        preferencesDelegate.saveLong("Hello1", Long.MAX_VALUE)

        val result = preferencesDelegate.loadLong("Hello1")

        assertEquals(Long.MAX_VALUE, result)
    }

    /**
     * Remove a previously saved key.
     */
    @Test
    fun testRemove() {
        preferencesDelegate.saveString("Hello1", "Test2")

        assertEquals("Test2", preferencesDelegate.loadString("Hello1"))

        preferencesDelegate.remove("Hello1")

        assertNull(preferencesDelegate.loadString("Hello1"))
    }

    /**
     * Try removing a non-exising key.
     */
    @Test
    fun testRemoveNonExisting() {
        preferencesDelegate.remove("Hello1")

        assertNull(preferencesDelegate.loadString("Hello1"))
    }
}
