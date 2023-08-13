package com.cramsan.framework.sample.jvm.application

import com.cramsan.sample.jvm.lib.JVMLib
import com.cramsan.sample.mpplib.MPPLib

/**
 * Simple JVM class.
 */
fun main() {
    val jvmLib = JVMLib()
    val mppLib = MPPLib()
    println("Welcome to a JVM application")
    println("Message from JVM Lib: ${jvmLib.getTarget()}")
    println("Message from MPP Lib: ${mppLib.getTarget()}")
}