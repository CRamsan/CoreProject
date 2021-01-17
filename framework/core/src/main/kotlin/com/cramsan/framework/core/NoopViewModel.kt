package com.cramsan.framework.core

import android.app.Application
import androidx.lifecycle.SavedStateHandle

class NoopViewModel constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(application, dispatcherProvider, SavedStateHandle()) {
    override val logTag: String
        get() = "NoopViewModel"
}
