package com.cramsan.framework.thread.implementation

import android.os.Handler
import android.os.Looper
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.thread.ThreadUtilDelegate
import java.util.concurrent.Executors

class ThreadUtilAndroid constructor(
    private val assertUtil: AssertUtilInterface
) : ThreadUtilDelegate {

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    private val pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    override fun isUIThread(): Boolean {
        return (Looper.myLooper() == mainHandler.looper)
    }

    override fun isBackgroundThread(): Boolean {
        return !isUIThread()
    }

    override fun assertIsUIThread() {
        assertUtil.assert(isUIThread(), "Test", "Not on UI thread!")
    }

    override fun assertIsBackgroundThread() {
        assertUtil.assert(isBackgroundThread(), "Test", "Not on background thread!")
    }
    companion object {
        private const val THREAD_POOL_SIZE = 10
    }
}
