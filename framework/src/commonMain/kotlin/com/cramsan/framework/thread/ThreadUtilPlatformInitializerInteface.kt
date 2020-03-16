package com.cramsan.framework.thread

import com.cramsan.framework.base.BaseModulePlatformInitializerInterface
import com.cramsan.framework.thread.implementation.ThreadUtilManifest

interface ThreadUtilPlatformInitializerInteface : BaseModulePlatformInitializerInterface<ThreadUtilManifest> {
    val platformThreadUtil: ThreadUtilInterface
}