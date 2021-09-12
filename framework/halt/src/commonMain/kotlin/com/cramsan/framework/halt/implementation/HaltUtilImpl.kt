package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate

/**
 * Module implementation of [HaltUtil]. Halting an application is very platform specific so all of
 * the logic is delegated to the [platformDelegate].
 */
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
