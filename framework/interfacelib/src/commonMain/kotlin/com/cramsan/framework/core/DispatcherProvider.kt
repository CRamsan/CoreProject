package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provider that allows to provide a dispatcher for specific use cases. This class comes in useful
 * when needing to inject different dispatcher based on the environment the code is runnig in.
 */
interface DispatcherProvider {

    /**
     * Dispatcher to be used for IO operations. This interface does not provide any guarantee of the
     * implementation of the dispatcher. It will be up to the caller to provide the correct implementation.
     */
    fun ioDispatcher(): CoroutineDispatcher

    /**
     * Dispatcher to be used for operations that need to be done in the UI thread.
     */
    fun uiDispatcher(): CoroutineDispatcher
}
