package com.cramsan.framework.crashehandler

import com.cramsan.framework.base.BaseModuleInterface
import com.cramsan.framework.crashehandler.implementation.CrashHandlerManifest

interface CrashHandlerInterface : BaseModuleInterface<CrashHandlerManifest> {
    fun initialize()
}
