package com.cramsan.framework.thread.implementation

import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilDelegate

class ThreadUtilJVM constructor(
    private val eventLogger: EventLoggerInterface,
    private val assertUtil: AssertUtilInterface
) : ThreadUtilDelegate {

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
        assertUtil.assert(false, "ThreadUtilJVM", "dispatchToUI: UIThread is not supported")
    }

    override fun assertIsUIThread() {
        assertUtil.assert(false, "ThreadUtilJVM", "assertIsUIThread: Always return false")
    }

    override fun assertIsBackgroundThread() {
        eventLogger.log(Severity.INFO, "ThreadUtilJVM", "assertIsBackgroundThread: Always returning True")
    }
}
