package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilDelegate

class HaltUtilAndroid : HaltUtilDelegate {

    private var shouldStop = true

    override fun stopThread() {
        shouldStop = true
        while (shouldStop) {
            Thread.sleep(sleepTime)
        }
    }

    override fun resumeThread() {
        shouldStop = false
    }

    override fun crashApp() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    companion object {
        private const val sleepTime = 1000L
    }
}
