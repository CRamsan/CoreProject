package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * This class provides a Lifecycle aware scope that uses [DispatcherProvider.ioDispatcher] as the
 * default dispatcher. This class was initially created for Repositories in Android applications but
 * it can be used by any type of application that requires to do IO heavy operations.
 */
class BackgroundModuleLifecycleAwareComponent(
    dispatcherProvider: DispatcherProvider,
) : LifecycleAwareComponent {

    private var _scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())

    override val scope = _scope

    override fun endScope() {
        scope.cancel()
    }
}
