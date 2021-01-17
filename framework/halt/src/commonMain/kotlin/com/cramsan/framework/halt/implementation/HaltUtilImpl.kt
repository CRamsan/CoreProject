package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate

class HaltUtilImpl(override val platformDelegate: HaltUtilDelegate) : HaltUtil {

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
