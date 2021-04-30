package com.cramsan.framework.core

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoopViewModel @Inject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(application, dispatcherProvider, SavedStateHandle()) {
    override val logTag: String
        get() = "NoopViewModel"
}
