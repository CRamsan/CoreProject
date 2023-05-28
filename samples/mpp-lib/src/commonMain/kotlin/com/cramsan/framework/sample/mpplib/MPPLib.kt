package com.cramsan.framework.sample.mpplib

/**
 * Simple MPP class. To be implemented on each platform target.
 *
 * @Author cramsan
 * @created 1/17/2021
 */
expect class MPPLib {

    /**
     * Simple MPP function. To be implemented on each platform target.
     */
    fun getTarget(): String
}

/**
 *
 */
fun greeting(lib: MPPLib) = "Hello from MPPLib. Current Platform: ${lib.getTarget()}"
