package com.cramsan.framework.assert.implementation

import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

/**
 * Standard implementation of [AssertUtilInterface].
 */
class AssertUtilImpl(
    override val haltOnFailure: Boolean,
    override val eventLogger: EventLoggerInterface?,
    override val haltUtil: HaltUtil?
) : AssertUtilInterface {

    /**
     * Ensure that [haltOnFailure] is not true while [haltUtil] is null
     */
    init {
        if (haltOnFailure && haltUtil == null) {
            throw RuntimeException("haltUtil cannot be null if haltOnFailure is set to true")
        }
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
        if (condition) {
            return
        }
        eventLogger?.log(Severity.ERROR, tag, message)
        if (haltOnFailure) {
            haltUtil?.stopThread()
        }
    }
}
