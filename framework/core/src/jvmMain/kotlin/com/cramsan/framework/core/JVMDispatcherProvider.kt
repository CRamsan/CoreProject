package com.cramsan.framework.core

import kotlinx.coroutines.Dispatchers

/**
 * JVM implementation of [DispatcherProvider]. It uses [Dispatchers.IO] for [ioDispatcher].
 */
class JVMDispatcherProvider : DispatcherProvider {

    @Suppress("InjectDispatcher")
    override fun ioDispatcher() = Dispatchers.IO

    override fun uiDispatcher() = Dispatchers.Main
}
