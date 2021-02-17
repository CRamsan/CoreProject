package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun ioDispatcher(): CoroutineDispatcher
}
