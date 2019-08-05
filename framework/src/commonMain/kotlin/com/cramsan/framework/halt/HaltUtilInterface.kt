package com.cramsan.framework.halt

interface HaltUtilInterface {
    fun resumeThread()
    fun stopThread()
    fun stopMainThread()
    fun crashApp()
}
