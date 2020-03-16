package com.cramsan.framework.thread.implementation

import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface

class ThreadUtilJVM constructor(
    private val eventLogger: EventLoggerInterface,
    private val assertUtil: AssertUtilInterface
) : ThreadUtilInterface {

    override fun isUIThread(): Boolean {
        return false
    }

    override fun isBackgroundThread(): Boolean {
        return true
    }

    override fun dispatchToBackground(block: RunBlock) {
        block()
    }

    override fun dispatchToUI(block: RunBlock) {
        assertUtil.assert(false, classTag(), "dispatchToUI: UIThread is not supported")
    }

    override fun assertIsUIThread() {
        assertUtil.assert(false, classTag(), "assertIsUIThread: Always return false")
    }

    override fun assertIsBackgroundThread() {
        eventLogger.log(Severity.INFO, classTag(), "assertIsBackgroundThread: Always returning True")
    }
}
