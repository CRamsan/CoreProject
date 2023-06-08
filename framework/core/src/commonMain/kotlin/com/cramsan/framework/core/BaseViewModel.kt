package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface BaseViewModel {

    val logTag: String

    val dispatcherProvider: DispatcherProvider

    val viewModelScope: CoroutineScope

    val events: SharedFlow<BaseEvent>

    fun close()
}
