package com.cramsan.petproject.framework.thread.implementation

import com.cramsan.petproject.framework.thread.RunBlock
import com.cramsan.petproject.framework.thread.ThreadUtilInterface

internal actual class ThreadUtil : ThreadUtilInterface {
    override fun isUIThread(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchToUI(block: RunBlock) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchToBackground(block: RunBlock) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun threadSleep(seconds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assertIsUIThread() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assertIsBackgroundThread() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}