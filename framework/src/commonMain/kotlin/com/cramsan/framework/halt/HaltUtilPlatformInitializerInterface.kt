package com.cramsan.framework.halt

import com.cramsan.framework.base.BaseModulePlatformInitializerInterface
import com.cramsan.framework.halt.implementation.HaltUtilManifest

interface HaltUtilPlatformInitializerInterface : BaseModulePlatformInitializerInterface<HaltUtilManifest> {
    val platformDelegate: HaltUtilInterface
}
