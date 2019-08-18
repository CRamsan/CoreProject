package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface

actual class HaltUtil actual constructor(eventLogger: EventLoggerInterface) : HaltUtilInterface {

    private var shouldStop = true

    override fun resumeThread() {
        while (shouldStop) {
            Thread.sleep(sleepTime)
        }
    }

    override fun stopThread() {
        shouldStop = false
    }

    override fun stopMainThread() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun crashApp() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val sleepTime = 1000L
    }
}
