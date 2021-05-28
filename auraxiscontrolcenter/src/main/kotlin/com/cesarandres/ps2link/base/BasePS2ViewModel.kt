package com.cesarandres.ps2link.base

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BasePS2ViewModel(
    application: Application,
    protected val pS2LinkRepository: PS2LinkRepository,
    protected val ps2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    fun loadingStarted() {
        _isLoading.value = true
        _isError.value = false
    }

    fun loadingCompleted() {
        _isLoading.value = false
        _isError.value = false
    }

    fun loadingCompletedWithError() {
        _isLoading.value = false
        _isError.value = true
    }
}
