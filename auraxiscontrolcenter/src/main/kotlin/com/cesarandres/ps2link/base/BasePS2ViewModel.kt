package com.cesarandres.ps2link.base

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BasePS2ViewModel constructor(
    application: Application,
    protected val dbgCensus: DBGServiceClient,
    protected val dbgDAO: DbgDAO,
    protected val ps2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadingStarted() {
        _isLoading.value = true
    }

    fun loadingCompleted() {
        _isLoading.value = false
    }
}
