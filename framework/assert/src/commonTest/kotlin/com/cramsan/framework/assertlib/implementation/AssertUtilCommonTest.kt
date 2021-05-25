package com.cramsan.framework.assertlib.implementation

import com.cramsan.framework.assertlib.AssertUtil
import com.cramsan.framework.assertlib.assert
import com.cramsan.framework.assertlib.assertFailure
import com.cramsan.framework.assertlib.assertFalse
import com.cramsan.framework.assertlib.assertNotNull
import com.cramsan.framework.assertlib.assertNull
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.test.TestBase
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 */
class AssertUtilCommonTest : TestBase() {

    @Test
    fun assertTrueWithHaltEnabled() = runBlockingTest {
        val eventLogger = mockkClass(EventLoggerImpl::class)
        val haltUtil = mockkClass(HaltUtilImpl::class)
        val assertUtil = AssertUtilImpl(true, eventLogger, haltUtil)

        assertUtil.assert(true, "Test", "Assert-Message-1")

        verify(exactly = 0) {
            eventLogger wasNot Called
            haltUtil wasNot Called
        }
    }

    @Test
    fun assertFalseWithHaltEnabled() = runBlockingTest {
        val eventLogger = mockkClass(EventLoggerImpl::class)
        val haltUtil = mockkClass(HaltUtilImpl::class)
        val assertUtil = AssertUtilImpl(true, eventLogger, haltUtil)
        val classTag = "Test"

        every { eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1") } just Runs
        every { haltUtil.stopThread() } just Runs

        assertUtil.assert(false, classTag, "Assert-Message-1")

        verify(exactly = 1) {
            eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1")
            haltUtil.stopThread()
        }
    }

    @Test
    fun assertTrueWithHaltDisabled() = runBlockingTest {
        val eventLogger = mockkClass(EventLoggerImpl::class)
        val haltUtil = mockkClass(HaltUtilImpl::class)
        val assertUtil = AssertUtilImpl(false, eventLogger, haltUtil)

        assertUtil.assert(true, "Test", "Assert-Message-1")

        verify(exactly = 0) {
            eventLogger wasNot Called
            haltUtil wasNot Called
        }
    }

    @Test
    fun assertFalseWithHaltDisabled() = runBlockingTest {
        val eventLogger = mockkClass(EventLoggerImpl::class)
        val haltUtil = mockkClass(HaltUtilImpl::class)
        val assertUtil = AssertUtilImpl(false, eventLogger, haltUtil)
        val classTag = "Test"

        every { eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1") } just Runs
        every { haltUtil.stopThread() } just Runs

        assertUtil.assert(false, "Test", "Assert-Message-1")

        verify(exactly = 1) {
            eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1")
        }
        verify(exactly = 0) {
            haltUtil.stopThread()
        }
    }

    @Test
    fun assertFailure() = runBlockingTest {
        val eventLogger = mockkClass(EventLoggerImpl::class)
        val haltUtil = mockkClass(HaltUtilImpl::class)
        val assertUtil = AssertUtilImpl(false, eventLogger, haltUtil)
        val classTag = "Test"

        every { eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1") } just Runs
        every { haltUtil.stopThread() } just Runs

        assertUtil.assertFailure("Test", "Assert-Message-1")

        verify(exactly = 1) {
            eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1")
        }
        verify(exactly = 0) {
            haltUtil.stopThread()
        }
    }

    @Test
    fun `test global functions`() = runBlockingTest {
        val assertUtil: AssertUtilImpl = mockk(relaxed = true)
        val tag = "TestTag"
        val message = "Error message"

        // Configure the singleton
        assertEquals(assertUtil, AssertUtil.instance(assertUtil))

        assert(true, tag, message)
        verify { assertUtil.assert(true, tag, message) }

        assert(false, tag, message)
        verify { assertUtil.assert(false, tag, message) }

        assertFalse(true, tag, message)
        verify { assertUtil.assert(false, tag, message) }

        assertNull(true, tag, message)
        verify { assertUtil.assert(false, tag, message) }

        assertNull(null, tag, message)
        verify { assertUtil.assert(true, tag, message) }

        assertNotNull(true, tag, message)
        verify { assertUtil.assert(true, tag, message) }

        assertNotNull(null, tag, message)
        verify { assertUtil.assert(false, tag, message) }

        assertFailure(tag, message)
        verify { assertUtil.assert(false, tag, message) }
    }
}
