package com.cramsan.framework.core

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.logging.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * The ViewModel holds the business logic that powers a screen for an Android application. This class
 * provides some helpful defaults. There is a [dispatcherProvider] that provides access to different
 * types of dispatchers, this is particularly useful for dispatching work to a background thread
 * without having to freeze the UI thread. [savedStateHandle] can be used to persist the state of the
 * viewModel in case the system needs to kill/restart the process. ViewModels already keep their state
 * on configuration change so [savedStateHandle] should not be used in those cases.
 */
abstract class BaseViewModel(
    application: Application,
    protected val dispatcherProvider: DispatcherProvider,
    protected val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    /**
     * String that identifies this class. Used for logging and telemetry.
     */
    abstract val logTag: String

    /**
     * This scope is tied to the viewModel's lifecycle and it uses [DispatcherProvider.ioDispatcher]
     * as the default dispatcher.
     */
    protected val ioScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())

    /**
     * TODO: Replace this implementation with one that provides a Flow based solution.
     */
    @Suppress("DEPRECATION")
    protected val events = LiveEvent<BaseEvent>()

    /**
     * LiveData that produce events of type [BaseEvent].
     */
    fun events() = events.asLiveData()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        logD(logTag, "onCleared")
        ioScope.cancel()
    }
}
