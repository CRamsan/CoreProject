package com.cramsan.framework.assert.implementation

import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.verify

class AssertUtilCommonTest {

    fun assertTrueWithHaltEnabled() {
        val initializer = AssertUtilInitializer(true)
        val eventLogger = mockkClass(EventLogger::class)
        val haltUtil = mockkClass(HaltUtil::class)
        val assertUtil = AssertUtil(initializer, eventLogger, haltUtil)

        assertUtil.assert(true, "Test", "Assert-Message-1")

        verify(exactly = 0) {
            eventLogger wasNot Called
            haltUtil wasNot Called
        }
    }

    fun assertFalseWithHaltEnabled() {
        val initializer = AssertUtilInitializer(true)
        val eventLogger = mockkClass(EventLogger::class)
        val haltUtil = mockkClass(HaltUtil::class)
        val assertUtil = AssertUtil(initializer, eventLogger, haltUtil)
        val classTag = "Test"

        every { eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1") } just Runs
        every { haltUtil.stopThread() } just Runs

        assertUtil.assert(false, classTag, "Assert-Message-1")

        verify(exactly = 1) {
            eventLogger.log(Severity.ERROR, classTag, "Assert-Message-1")
            haltUtil.stopThread()
        }
    }

    fun assertTrueWithHaltDisabled() {
        val initializer = AssertUtilInitializer(false)
        val eventLogger = mockkClass(EventLogger::class)
        val haltUtil = mockkClass(HaltUtil::class)
        val assertUtil = AssertUtil(initializer, eventLogger, haltUtil)

        assertUtil.assert(true, "Test", "Assert-Message-1")

        verify(exactly = 0) {
            eventLogger wasNot Called
            haltUtil wasNot Called
        }
    }

    fun assertFalseWithHaltDisabled() {
        val initializer = AssertUtilInitializer(false)
        val eventLogger = mockkClass(EventLogger::class)
        val haltUtil = mockkClass(HaltUtil::class)
        val assertUtil = AssertUtil(initializer, eventLogger, haltUtil)
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
}
