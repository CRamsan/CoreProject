package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class BackgroundModuleLifecycleAwareComponent(
    dispatcherProvider: DispatcherProvider,
) : LifecycleAwareComponent {

    private var _scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())

    override val scope = _scope

    override fun endScope() {
        scope.cancel()
    }
}
