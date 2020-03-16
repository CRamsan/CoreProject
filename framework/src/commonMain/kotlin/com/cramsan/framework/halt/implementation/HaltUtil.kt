package com.cramsan.framework.halt.implementation

import com.cramsan.framework.base.implementation.BaseModule
import com.cramsan.framework.halt.HaltUtilInterface

class HaltUtil(initializer: HaltUtilInitializer) : BaseModule<HaltUtilManifest>(initializer), HaltUtilInterface {

    private val platformHaltUtil = initializer.platformInitializer.platformDelegate

    override fun resumeThread() {
        platformHaltUtil.resumeThread()
    }

    override fun stopThread() {
        platformHaltUtil.stopThread()
    }

    override fun crashApp() {
        platformHaltUtil.crashApp()
    }
}
