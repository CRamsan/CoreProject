package com.cramsan.framework.assert.implementation

import org.junit.Before
import org.junit.Test

/**
 *
 */
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
