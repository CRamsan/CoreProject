package com.cramsan.framework.thread.implementation

import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface

class ThreadUtil(private val platformDelegate: ThreadUtilInterface) : ThreadUtilInterface {

    override fun isUIThread(): Boolean {
        return platformDelegate.isUIThread()
    }

    override fun isBackgroundThread(): Boolean {
        return platformDelegate.isBackgroundThread()
    }

    override fun dispatchToBackground(block: RunBlock) {
        return platformDelegate.dispatchToBackground(block)
    }

    override fun dispatchToUI(block: RunBlock) {
        platformDelegate.dispatchToUI(block)
    }

    override fun assertIsUIThread() {
        platformDelegate.assertIsUIThread()
    }

    override fun assertIsBackgroundThread() {
        platformDelegate.assertIsBackgroundThread()
    }
}
