package com.cramsan.framework.assert.implementation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit test. This will be executed in a mocked Android environment.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AssertUtilTest {

    private lateinit var assertUtilTest: AssertUtilCommonTest

    @Before
    fun setUp() {
        assertUtilTest = AssertUtilCommonTest()
    }

    @Test
    fun assertTrueWithHaltEnabled() {
        assertUtilTest.assertTrueWithHaltEnabled()
    }

    @Test
    fun assertFalseWithHaltEnabled() {
        assertUtilTest.assertFalseWithHaltEnabled()
    }

    @Test
    fun assertTrueWithHaltDisabled() {
        assertUtilTest.assertTrueWithHaltDisabled()
    }

    @Test
    fun assertFalseWithHaltDisabled() {
        assertUtilTest.assertFalseWithHaltDisabled()
    }
}