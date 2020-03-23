package com.cramsan.framework.thread.implementation

import com.cramsan.framework.base.implementation.BaseModule
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface

class ThreadUtil(initializer: ThreadUtilInitializer) : BaseModule<ThreadUtilManifest>(initializer), ThreadUtilInterface {

    private val platformThreadUtil = initializer.platformInitializer.platformThreadUtil

    override fun isUIThread(): Boolean {
        return platformThreadUtil.isUIThread()
    }

    override fun isBackgroundThread(): Boolean {
        return platformThreadUtil.isBackgroundThread()
    }

    override fun dispatchToBackground(block: RunBlock) {
        return platformThreadUtil.dispatchToBackground(block)
    }

    override fun dispatchToUI(block: RunBlock) {
        platformThreadUtil.dispatchToUI(block)
    }

    override fun assertIsUIThread() {
        platformThreadUtil.assertIsUIThread()
    }

    override fun assertIsBackgroundThread() {
        platformThreadUtil.assertIsBackgroundThread()
    }
}
