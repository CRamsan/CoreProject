package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.BaseViewModelImpl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *
 */
abstract class BasePS2ViewModel(
    override val pS2LinkRepository: PS2LinkRepository,
    override val ps2Settings: PS2Settings,
    override val languageProvider: LanguageProvider,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModelImpl<BasePS2Event>(dispatcherProvider), BasePS2ViewModelInterface {

    private val _isLoading = MutableStateFlow(false)
    override val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    override val isError = _isError.asStateFlow()

    override val events: SharedFlow<BasePS2Event> = _events.asSharedFlow()

    override fun loadingStarted() {
        _isLoading.value = true
        _isError.value = false
    }

    override fun loadingCompleted() {
        _isLoading.value = false
        _isError.value = false
    }

    override fun loadingCompletedWithError() {
        _isLoading.value = false
        _isError.value = true
    }
}

/**
 *
 */
interface BasePS2ViewModelInterface : BaseViewModel {

    val pS2LinkRepository: PS2LinkRepository

    val ps2Settings: PS2Settings

    val languageProvider: LanguageProvider

    val isLoading: StateFlow<Boolean>

    val isError: StateFlow<Boolean>

    /**
     *
     */
    fun loadingStarted()

    /**
     *
     */
    fun loadingCompleted()

    /**
     *
     */
    fun loadingCompletedWithError()
}
