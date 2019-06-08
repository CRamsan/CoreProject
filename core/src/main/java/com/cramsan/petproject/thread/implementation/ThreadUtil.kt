package com.cramsan.petproject.thread.implementation

import android.os.Handler
import android.os.Looper
import com.cramsan.petproject.framework.CoreFramework
import com.cramsan.petproject.thread.RunBlock
import com.cramsan.petproject.thread.ThreadUtilInterface

internal actual class ThreadUtil : ThreadUtilInterface {

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
        CoreFramework.eventLogger.assert(isUIThread(), "", "Not on UI thread!")
    }

    override fun assertIsBackgroundThread() {
        CoreFramework.eventLogger.assert(isUIThread(), "", "Not on background thread!")
    }
}