package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

/**
 * Common-code interface for all viewmodels. This interface is expected to be implemented in common
 * code with a platform-specific shim.
 */
interface BaseViewModel {

    val logTag: String

    val dispatcherProvider: DispatcherProvider

    val viewModelScope: CoroutineScope

    val events: SharedFlow<BaseEvent>

    /**
     * Called when this viewmodel will not longer be used and all resources can be released. All
     * calls to this VM after this function is called will have undefined behaviour.
     */
    fun close()
}
