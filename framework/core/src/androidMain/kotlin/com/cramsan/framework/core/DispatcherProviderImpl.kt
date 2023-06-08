package com.cramsan.framework.core

import kotlinx.coroutines.Dispatchers

/**
 * Android implementation of [DispatcherProvider]. It uses [Dispatchers.IO] for [ioDispatcher].
 */
class DispatcherProviderImpl : DispatcherProvider {

    @Suppress("InjectDispatcher")
    override fun ioDispatcher() = Dispatchers.IO
    override fun uiDispatcher() = Dispatchers.Main
}
