package com.cramsan.framework.thread

typealias RunBlock = () -> Unit

interface ThreadUtilInterface {
    fun isUIThread(): Boolean
    fun dispatchToUI(block: RunBlock)
    fun dispatchToBackground(block: RunBlock)
    fun assertIsUIThread()
    fun assertIsBackgroundThread()

    fun threadSleep(seconds: Int)
}