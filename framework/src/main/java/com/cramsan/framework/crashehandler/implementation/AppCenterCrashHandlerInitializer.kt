package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.crashehandler.CrashHandlerInterface
import com.cramsan.framework.crashehandler.CrashHandlerPlatformInitializerInterface

class AppCenterCrashHandlerInitializer(
    override val platformDelegate: CrashHandlerInterface
) : CrashHandlerPlatformInitializerInterface
