package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.base.implementation.BaseModuleInitializer
import com.cramsan.framework.crashehandler.CrashHandlerPlatformInitializerInterface

class CrashHandlerInitializer(val platformInitializer: CrashHandlerPlatformInitializerInterface) :
    BaseModuleInitializer<CrashHandlerManifest>(platformInitializer)
