package com.cramsan.framework.crashehandler.implementation

import com.cramsan.framework.base.implementation.BaseModule
import com.cramsan.framework.crashehandler.CrashHandlerInterface

class CrashHandler(val initializer: CrashHandlerInitializer) : BaseModule<CrashHandlerManifest>(initializer), CrashHandlerInterface {

    override fun initialize() {
        initializer.platformInitializer.platformDelegate.initialize()
    }
}
