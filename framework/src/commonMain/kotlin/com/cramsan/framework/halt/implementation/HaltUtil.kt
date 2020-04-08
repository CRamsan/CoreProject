package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface

class HaltUtil(private val platformDelegate: HaltUtilInterface) : HaltUtilInterface {

    override fun resumeThread() {
        platformDelegate.resumeThread()
    }

    override fun stopThread() {
        platformDelegate.stopThread()
    }

    override fun crashApp() {
        platformDelegate.crashApp()
    }
}
