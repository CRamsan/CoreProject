package com.cramsan.framework.logging

/**
 * @Author cramsan
 * @created 1/17/2021
 */
object EventLogger {

    lateinit var singleton: EventLoggerInterface

    fun instance(eventLogger: EventLoggerInterface? = null): EventLoggerInterface {
        eventLogger?.let {
            singleton = it
        }
        return singleton
    }
}

/**
 * List of global functions to provide an easy API for logging
 */

fun logV(tag: String, message: String) {
    EventLogger.singleton.v(tag, message)
}

fun logD(tag: String, message: String) {
    EventLogger.singleton.d(tag, message)
}

fun logI(tag: String, message: String) {
    EventLogger.singleton.i(tag, message)
}

fun logW(tag: String, message: String, throwable: Throwable? = null) {
    EventLogger.singleton.w(tag, message, throwable)
}

fun logE(tag: String, message: String, throwable: Throwable? = null) {
    EventLogger.singleton.e(tag, message, throwable)
}
