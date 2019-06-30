package com.cramsan.framework.thread.implementation

import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface

class MockThreadUtil : ThreadUtilInterface {
    override fun isUIThread(): Boolean {
        return true
    }

    override fun isBackgroundThread(): Boolean {
        return false
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
