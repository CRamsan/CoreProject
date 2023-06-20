package com.cramsan.framework.core

import com.cramsan.framework.logging.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Implementation of [BaseViewModel] that provides some safe defaults to build upon.
 */
abstract class BaseViewModelImpl<E : BaseEvent>(
    override val dispatcherProvider: DispatcherProvider,
) : BaseViewModel {

    /**
     * This scope is tied to the viewModel's lifecycle and it uses [DispatcherProvider.uiDispatcher]
     * as the default dispatcher.
     */
    override val viewModelScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + dispatcherProvider.uiDispatcher()
    )

    /**
     * Emit values that should be consumed by the UI layer.
     */
    @Suppress("VariableNaming")
    protected val _events = MutableSharedFlow<E>()

    /**
     * LiveData that produce events of type [BaseEvent].
     */
    override val events = _events.asSharedFlow()

    /**
     * Call this function to signify that this ViewModel's operation are concluded and any remaining
     * work can be cancelled.
     */
    override fun close() {
        logD(logTag, "close")
        viewModelScope.cancel()
    }
}
