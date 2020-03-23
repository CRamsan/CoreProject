package com.cramsan.framework.halt.implementation

import com.cramsan.framework.base.implementation.BaseModuleInitializer
import com.cramsan.framework.halt.HaltUtilPlatformInitializerInterface

class HaltUtilInitializer(val platformInitializer: HaltUtilPlatformInitializerInterface) : BaseModuleInitializer<HaltUtilManifest>(platformInitializer)
