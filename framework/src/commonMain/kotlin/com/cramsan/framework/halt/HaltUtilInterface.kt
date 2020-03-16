package com.cramsan.framework.halt

import com.cramsan.framework.base.BaseModuleInterface
import com.cramsan.framework.halt.implementation.HaltUtilManifest

interface HaltUtilInterface : BaseModuleInterface<HaltUtilManifest>{
    fun resumeThread()
    fun stopThread()
    fun crashApp()
}
