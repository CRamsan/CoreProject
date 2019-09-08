package com.cramsan.framework.thread.implementation

import android.os.Handler
import android.os.Looper
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface
import java.util.concurrent.Executors

actual class ThreadUtil actual constructor(
    private val eventLogger: EventLoggerInterface,
    private val assertUtil: AssertUtilInterface
) : ThreadUtilInterface {

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    private val pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    override fun isUIThread(): Boolean {
        return (Looper.myLooper() == mainHandler.looper)
    }

    override fun isBackgroundThread(): Boolean {
        return !isUIThread()
    }

    override fun dispatchToUI(block: RunBlock) {
        assertUtil.assert(false, classTag(), "On Android we should not dispatch to the UI thread.")
        if (isUIThread()) {
            block()
        } else {
            mainHandler.post(block)
        }
    }

    override fun dispatchToBackground(block: RunBlock) {
        pool.execute(block)
    }

    override fun assertIsUIThread() {
        assertUtil.assert(isUIThread(), classTag(), "Not on UI thread!")
    }

    override fun assertIsBackgroundThread() {
        assertUtil.assert(isBackgroundThread(), classTag(), "Not on background thread!")
    }
    companion object {
        private const val THREAD_POOL_SIZE = 10
    }
}
