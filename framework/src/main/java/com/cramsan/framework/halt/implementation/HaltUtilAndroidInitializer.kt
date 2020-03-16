package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.HaltUtilPlatformInitializerInterface

class HaltUtilAndroidInitializer(override val platformDelegate: HaltUtilInterface
) : HaltUtilPlatformInitializerInterface