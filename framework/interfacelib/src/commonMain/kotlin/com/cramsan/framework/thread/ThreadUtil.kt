package com.cramsan.framework.thread

/**
 * @Author cramsan
 * @created 1/17/2021
 */
object ThreadUtil {
    lateinit var singleton: ThreadUtilInterface

    fun instance(eventLogger: ThreadUtilInterface? = null): ThreadUtilInterface {
        eventLogger?.let {
            singleton = it
        }
        return singleton
    }
}

/**
 * List of global functions to provide an easy API for threat utilities
 */

fun isUIThread() {
    ThreadUtil.singleton.isUIThread()
}

fun isBackgroundThread() {
    ThreadUtil.singleton.isBackgroundThread()
}

fun assertIsUIThread() {
    ThreadUtil.singleton.assertIsUIThread()
}

fun assertIsBackgroundThread() {
    ThreadUtil.singleton.assertIsBackgroundThread()
}
