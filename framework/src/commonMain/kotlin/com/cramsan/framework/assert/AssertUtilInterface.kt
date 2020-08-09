package com.cramsan.framework.assert

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface

/**
 * [AssertUtilInterface]
 * This module allows to halt the current thread when an assertion fails. This module can be configured
 * by setting [haltOnFailure] to false if you want to disable this behaviour. If [haltOnFailure] is true,
 * then [haltUtil] needs to be non-null.
 */
interface AssertUtilInterface {

    /**
     * If true, then when [assert] is called with a false condition, the current thread will be halted
     */
    val haltOnFailure: Boolean

    /**
     * Optional logger to log in case of a failed assertion
     */
    val eventLogger: EventLoggerInterface?

    /**
     * Optional [HaltUtilInterface]. It is only needed if [haltOnFailure] is set to true
     */
    val haltUtil: HaltUtilInterface?

    /**
     * If the [condition] is true, this function does not do anything. Otherwise this function may
     * halt the current thread based on the [haltOnFailure] and [haltUtil]. When the assertion
     * fails, [tag] and [message] may be used to log a message by using the [eventLogger].
     */
    fun assert(condition: Boolean, tag: String, message: String)
}
