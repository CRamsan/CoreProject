package com.cramsan.petproject.base

import android.app.Application

open class NoopViewModel(application: Application) : BaseViewModel(application) {
    override val logTag: String
        get() = "NoopViewModel"
}
