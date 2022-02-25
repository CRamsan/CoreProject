package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilDelegate
import kotlin.system.exitProcess

/**
 * JVM implementation of [HaltUtilDelegate]. This implementation uses a spin-lock for halting a thread.
 */
class HaltUtilJVM : HaltUtilDelegate {

    // TODO: Refactor this into an AtomicBoolean
    private var shouldStop = false

    override fun resumeThread() {
        shouldStop = false
    }

    override fun stopThread() {
        shouldStop = true
        while (shouldStop) {
            Thread.sleep(sleepTime)
        }
    }

    override fun crashApp() {
        exitProcess(1)
    }

    companion object {
        private const val sleepTime = 1000L
    }
}
