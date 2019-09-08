package com.cramsan.framework.thread.implementation

import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface

actual class ThreadUtil actual constructor(
    eventLogger: EventLoggerInterface,
    assertUtil: AssertUtilInterface
) : ThreadUtilInterface {

    override fun isUIThread(): Boolean {
        return false
    }

    override fun isBackgroundThread(): Boolean {
        return true
    }

    override fun dispatchToBackground(block: RunBlock) {
    }

    override fun dispatchToUI(block: RunBlock) {
    }

    override fun assertIsUIThread() {
    }

    override fun assertIsBackgroundThread() {
    }
}
