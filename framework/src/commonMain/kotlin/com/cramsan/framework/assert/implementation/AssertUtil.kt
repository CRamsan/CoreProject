package com.cramsan.framework.assert.implementation

import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

class AssertUtil(private val haltOnFailure: Boolean,
                 private val eventLogger: EventLoggerInterface,
                 private val haltUtil: HaltUtilInterface): AssertUtilInterface {

    override fun assert(condition: Boolean, tag: String, message: String) {
        if (condition) {
            return
        }
        eventLogger.log(Severity.ERROR, tag, message)
        if (haltOnFailure) {
            haltUtil.stopThread()
        }
    }
}
