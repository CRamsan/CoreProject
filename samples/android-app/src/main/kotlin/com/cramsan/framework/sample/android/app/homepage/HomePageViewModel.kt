package com.cramsan.framework.sample.android.app.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cramsan.framework.sample.android.app.UIEvent
import com.cramsan.framework.sample.androidlib.Library
import com.cramsan.sample.jvm.lib.JVMLib
import com.cramsan.sample.mpplib.MPPLib
import com.cramsan.sample.mpplib.greeting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Unhandled exception in VM: $throwable")
    }

    private val viewModelScope = CoroutineScope(SupervisorJob() + exceptionHandler + Dispatchers.Main)

    private val _uiState = MutableStateFlow(HomePageUIState.InitialState)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableStateFlow<UIEvent>(UIEvent.Noop)
    val events = _events.asStateFlow()

    /**
     *
     */
    fun loadData(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                if (forceRefresh) {
                    delay(1000)
                }
                delay(100)
            } finally {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    title = greeting(MPPLib()),
                    subtitle = JVMLib().getTarget(),
                    message = Library().getValue(),
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    companion object {
        private const val TAG = "HomePageViewModel"
    }
}
