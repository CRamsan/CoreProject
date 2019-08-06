package com.cramsan.framework.thread.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface

actual class ThreadUtil actual constructor(
    eventLogger: EventLoggerInterface
) : ThreadUtilInterface {
    override fun isBackgroundThread(): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun isUIThread(): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchToUI(block: RunBlock) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchToBackground(block: RunBlock) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun assertIsUIThread() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun assertIsBackgroundThread() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
