package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface

actual class HaltUtil actual constructor(
    initializer: HaltUtilInitializer,
    eventLogger: EventLoggerInterface
) :
    HaltUtilInterface {

    override fun stopThread() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun resumeThread() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun stopMainThread() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun crashApp() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
