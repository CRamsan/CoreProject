package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface

actual class HaltUtil actual constructor(
    private val eventLogger: EventLoggerInterface
) : HaltUtilInterface {

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
