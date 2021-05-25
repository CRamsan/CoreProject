package com.cramsan.framework.assertlib

/**
 * @Author cramsan
 * @created 1/17/2021
 */
object AssertUtil {

    lateinit var singleton: AssertUtilInterface

    fun instance(assertUtil: AssertUtilInterface? = null): AssertUtilInterface {
        assertUtil?.let {
            singleton = it
        }
        return singleton
    }
}

/**
 * List of global functions to provide an easy API for assertions
 */

fun assert(condition: Boolean, tag: String, message: String) {
    AssertUtil.singleton.assert(condition, tag, message)
}

fun assertFalse(condition: Boolean, tag: String, message: String) {
    AssertUtil.singleton.assert(!condition, tag, message)
}

fun assertNull(any: Any?, tag: String, message: String) {
    AssertUtil.singleton.assert(any == null, tag, message)
}

fun assertNotNull(any: Any?, tag: String, message: String) {
    AssertUtil.singleton.assert(any != null, tag, message)
}

fun assertFailure(tag: String, message: String) {
    AssertUtil.singleton.assert(false, tag, message)
}
