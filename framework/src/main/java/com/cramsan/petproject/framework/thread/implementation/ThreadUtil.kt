package com.cramsan.petproject.framework.thread.implementation

import android.os.Handler
import android.os.Looper
import com.cramsan.petproject.framework.logging.EventLoggerInterface
import com.cramsan.petproject.framework.thread.RunBlock
import com.cramsan.petproject.framework.thread.ThreadUtilInterface

internal actual class ThreadUtil actual constructor(initializer: ThreadUtilInitializer) : ThreadUtilInterface {

    private var logger: EventLoggerInterface = initializer.eventLoggerInterface

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun isUIThread(): Boolean {
        return (Looper.myLooper() == mainHandler.looper)
    }

    override fun dispatchToUI(block: RunBlock) {
        if (isUIThread()) {
            block()
        } else {
            mainHandler.post { block() }
        }
    }

    override fun dispatchToBackground(block: RunBlock) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assertIsUIThread() {
        logger.assert(isUIThread(), "", "Not on UI thread!")
    }

    override fun assertIsBackgroundThread() {
        logger.assert(isUIThread(), "", "Not on background thread!")
    }

    override fun threadSleep(seconds: Int) {
        Thread.sleep((seconds * 1000).toLong())
    }
}