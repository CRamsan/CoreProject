package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun mainDispatcher(): CoroutineDispatcher

    fun ioDispatcher(): CoroutineDispatcher
}
