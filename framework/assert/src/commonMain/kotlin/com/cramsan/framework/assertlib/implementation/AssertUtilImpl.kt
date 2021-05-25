package com.cramsan.framework.assertlib.implementation

import com.cramsan.framework.assertlib.AssertUtilInterface
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

    private fun assertImpl(condition: Boolean, tag: String, message: String) {
        if (condition) {
            return
        }
        eventLogger?.log(Severity.ERROR, tag, message)
        if (haltOnFailure) {
            haltUtil?.stopThread()
        }
    }

    override fun assert(condition: Boolean, tag: String, message: String) {
        assertImpl(condition, tag, message)
    }

    override fun assertFalse(condition: Boolean, tag: String, message: String) {
        assertImpl(!condition, tag, message)
    }

    override fun assertNull(any: Any?, tag: String, message: String) {
        assertImpl(any == null, tag, message)
    }

    override fun assertNotNull(any: Any?, tag: String, message: String) {
        assertImpl(any != null, tag, message)
    }

    override fun assertFailure(tag: String, message: String) {
        assertImpl(false, tag, message)
    }
}
