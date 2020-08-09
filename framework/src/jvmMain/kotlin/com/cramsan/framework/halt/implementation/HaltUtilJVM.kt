package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilDelegate
import kotlin.system.exitProcess

class HaltUtilJVM : HaltUtilDelegate {

    private var shouldStop = true

    override fun resumeThread() {
        while (shouldStop) {
            Thread.sleep(sleepTime)
        }
    }

    override fun stopThread() {
        shouldStop = false
    }

    override fun crashApp() {
        exitProcess(1)
    }

    companion object {
        private const val sleepTime = 1000L
    }
}
