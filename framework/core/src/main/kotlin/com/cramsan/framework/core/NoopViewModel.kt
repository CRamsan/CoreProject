package com.cramsan.framework.core

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle

class NoopViewModel @ViewModelInject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(application, dispatcherProvider, SavedStateHandle()) {
    override val logTag: String
        get() = "NoopViewModel"
}
