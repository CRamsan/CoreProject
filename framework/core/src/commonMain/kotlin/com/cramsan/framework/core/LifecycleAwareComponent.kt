package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineScope

/**
 * TODO: Rename this class to prevent confusion with Android's Lifecycle-aware components.
 *
 * Provide a [CoroutineScope] that can lives until [endScope] is called. It is important to note that
 * managing the lifecycle is completely application dependent, so whoever is managing the lifecycle
 * will take on the responsibility to initialize this class and then call [endScope].
 */
interface LifecycleAwareComponent {

    /**
     * Coroutine scope that is managed by this instance. Calls to [endScope] should
     * stop the scope. The scope is not expected to be able to restart the scope.
     */
    val scope: CoroutineScope

    /**
     * End the [scope]. This function should be called when this component is being disposed.
     */
    fun endScope()
}
