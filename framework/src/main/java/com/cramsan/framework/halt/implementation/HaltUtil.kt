package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface

actual class HaltUtil actual constructor(initializer: HaltUtilInitializer,
                                         eventLogger: EventLoggerInterface) :
    HaltUtilInterface {

    private var shouldStop = true

    override fun stopThread() {
        while (shouldStop) {
            Thread.sleep(1000)
        }
    }

    override fun resumeThread() {
        shouldStop = false
    }

    override fun stopMainThread() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun crashApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}