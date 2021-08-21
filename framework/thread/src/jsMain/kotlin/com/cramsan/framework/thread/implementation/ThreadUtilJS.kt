package com.cramsan.framework.thread.implementation

import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilDelegate

class ThreadUtilJS constructor(
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
        assertUtil.assert(false, "ThreadUtilJS", "dispatchToUI: UIThread is not supported")
    }

    override fun assertIsUIThread() {
        assertUtil.assert(false, "ThreadUtilJS", "assertIsUIThread: Always return false")
    }

    override fun assertIsBackgroundThread() {
        eventLogger.log(Severity.INFO, "ThreadUtilJS", "assertIsBackgroundThread: Always returning True")
    }
}
