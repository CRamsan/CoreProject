package com.cramsan.framework.thread.implementation

import android.os.Handler
import android.os.Looper
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.getTag
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilInterface
import java.util.concurrent.Executors

internal actual class ThreadUtil actual constructor(initializer: ThreadUtilInitializer) : ThreadUtilInterface {

    private var logger: EventLoggerInterface = initializer.eventLoggerInterface

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    private val pool = Executors.newFixedThreadPool(10)

    override fun isUIThread(): Boolean {
        return (Looper.myLooper() == mainHandler.looper)
    }

    override fun isBackgroundThread(): Boolean {
        return !isUIThread()
    }

    override fun dispatchToUI(block: RunBlock) {
        logger.assert(false, getTag(), "On Android we should not dispatch to the UI thread.")
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
        logger.assert(isUIThread(), getTag(), "Not on UI thread!")
    }

    override fun assertIsBackgroundThread() {
        logger.assert(isBackgroundThread(), getTag(), "Not on background thread!")
    }
}