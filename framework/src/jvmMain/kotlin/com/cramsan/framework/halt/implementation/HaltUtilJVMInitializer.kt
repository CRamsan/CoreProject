package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.HaltUtilPlatformInitializerInterface

class HaltUtilJVMInitializer(override val platformDelegate: HaltUtilInterface
) : HaltUtilPlatformInitializerInterface