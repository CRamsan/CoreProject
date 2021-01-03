package com.cramsan.framework.core

import kotlinx.coroutines.Dispatchers

class DispatcherProviderImpl : DispatcherProvider {

    override fun mainDispatcher() = Dispatchers.Main

    override fun ioDispatcher() = Dispatchers.IO
}
