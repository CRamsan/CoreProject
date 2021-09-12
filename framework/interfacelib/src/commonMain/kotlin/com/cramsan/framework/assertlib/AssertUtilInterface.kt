package com.cramsan.framework.assertlib

import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface

/**
 * [AssertUtilInterface]
 * This module allows to halt the current thread when an assertion fails. This module can be configured
 * by setting [haltOnFailure] to false if you want to disable this behaviour. If [haltOnFailure] is true,
 * then [haltUtil] needs to be non-null.
 */
interface AssertUtilInterface {

    /**
     * If true, then when [assert] is called with a false condition, the current thread will be halted.
     */
    val haltOnFailure: Boolean

    /**
     * Optional logger to log in case of a failed assertion.
     */
    val eventLogger: EventLoggerInterface?

    /**
     * Optional [HaltUtil]. It is only needed if [haltOnFailure] is set to true.
     */
    val haltUtil: HaltUtil?

    /**
     * If the [condition] is true, this function does not do anything. Otherwise this function may
     * halt the current thread based on the value of [haltOnFailure] and [haltUtil]. When the assertion
     * fails, [tag] and [message] may be used to log a message by using the [eventLogger].
     */
    fun assert(condition: Boolean, tag: String, message: String)

    /**
     * If the [condition] is false, this function does not do anything. Otherwise this function may
     * halt the current thread based on the value of [haltOnFailure] and [haltUtil]. When the assertion
     * fails, [tag] and [message] may be used to log a message by using the [eventLogger].
     */
    fun assertFalse(condition: Boolean, tag: String, message: String)

    /**
     * If [any] is null, this function does not do anything. Otherwise this function may
     * halt the current thread based on the value of [haltOnFailure] and [haltUtil]. When the assertion
     * fails, [tag] and [message] may be used to log a message by using the [eventLogger].
     */
    fun assertNull(any: Any?, tag: String, message: String)

    /**
     * If [any] is not null, this function does not do anything. Otherwise this function may
     * halt the current thread based on the value of [haltOnFailure] and [haltUtil]. When the assertion
     * fails, [tag] and [message] may be used to log a message by using the [eventLogger].
     */
    fun assertNotNull(any: Any?, tag: String, message: String)

    /**
     * Function to always assert. This function may halt the current thread based on [haltOnFailure]
     * and [haltUtil]. [tag] and [message] may be used to log a message by using the [eventLogger].
     */
    fun assertFailure(tag: String, message: String)
}
