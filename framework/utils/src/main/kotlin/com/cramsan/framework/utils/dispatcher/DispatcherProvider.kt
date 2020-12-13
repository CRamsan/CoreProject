package com.cramsan.framework.utils.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun mainDispatcher(): CoroutineDispatcher

    fun ioDispatcher(): CoroutineDispatcher
}
