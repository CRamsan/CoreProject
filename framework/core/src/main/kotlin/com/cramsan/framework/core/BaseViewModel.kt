package com.cramsan.framework.core

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.logging.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseViewModel(
    application: Application,
    protected val dispatcherProvider: DispatcherProvider,
    protected val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    protected abstract val logTag: String

    protected val ioScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())

    protected val events = LiveEvent<BaseEvent>()
    fun events() = events.asLiveData()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        logD(logTag, "onCleared")
        ioScope.cancel()
    }
}
