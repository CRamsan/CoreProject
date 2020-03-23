package com.cramsan.framework.thread

import com.cramsan.framework.base.BaseModuleInterface
import com.cramsan.framework.thread.implementation.ThreadUtilManifest

typealias RunBlock = () -> Unit

interface ThreadUtilInterface : BaseModuleInterface<ThreadUtilManifest> {
    fun isUIThread(): Boolean
    fun isBackgroundThread(): Boolean
    fun dispatchToBackground(block: RunBlock)
    fun dispatchToUI(block: RunBlock)
    fun assertIsUIThread()
    fun assertIsBackgroundThread()
}
