package com.cramsan.framework.thread

typealias RunBlock = () -> Unit

interface ThreadUtilInterface {
    fun isUIThread(): Boolean
    fun isBackgroundThread(): Boolean
    fun dispatchToBackground(block: RunBlock)
    fun dispatchToUI(block: RunBlock)
    fun assertIsUIThread()
    fun assertIsBackgroundThread()
}