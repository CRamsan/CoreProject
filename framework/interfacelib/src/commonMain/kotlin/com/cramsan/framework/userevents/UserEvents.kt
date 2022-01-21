package com.cramsan.framework.userevents

import com.cramsan.framework.assertlib.AssertUtil.singleton
import com.cramsan.framework.logging.EventLogger.singleton
import kotlin.native.concurrent.ThreadLocal

/**
 * Singleton that manages an instance of an [UserEventsInterface]. The [singleton] starts as
 * null and therefore the caller needs to ensure to provide an instance. If an
 * instance is not set, then any calls to a method that tries to access it will result in a
 * [Throwable] being thrown.
 *
 * @Author cramsan
 * @created 1/17/2021
 *
 */
@ThreadLocal
object UserEvents {

    private lateinit var _singleton: UserEventsInterface

    /**
     * Global [UserEventsInterface] singleton
     */
    val singleton: UserEventsInterface
        get() = _singleton

    /**
     * Set the instance to be used for the [singleton].
     */
    fun setInstance(assertUtil: UserEventsInterface) {
        _singleton = assertUtil
    }
}

/**
 * List of global functions to provide an easy API for logging metrics
 */

/**
 * Global function that delegates to [UserEvents.singleton] and calls [UserEventsInterface.log].
 *
 * @see UserEventsInterface.log
 * @see UserEvents.singleton
 */
fun logEvent(tag: String, event: String) {
    UserEvents.singleton.log(tag, event)
}

/**
 * Global function that delegates to [UserEvents.singleton] and calls [UserEventsInterface.log].
 *
 * @see UserEventsInterface.log
 * @see UserEvents.singleton
 */
fun logEvent(tag: String, event: String, metadata: Map<String, String>) {
    UserEvents.singleton.log(tag, event, metadata)
}
