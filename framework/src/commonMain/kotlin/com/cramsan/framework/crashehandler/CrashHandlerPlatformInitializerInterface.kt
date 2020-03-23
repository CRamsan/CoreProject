package com.cramsan.framework.crashehandler

import com.cramsan.framework.base.BaseModulePlatformInitializerInterface
import com.cramsan.framework.crashehandler.implementation.CrashHandlerManifest

interface CrashHandlerPlatformInitializerInterface : BaseModulePlatformInitializerInterface<CrashHandlerManifest> {
    val platformDelegate: CrashHandlerInterface
}
