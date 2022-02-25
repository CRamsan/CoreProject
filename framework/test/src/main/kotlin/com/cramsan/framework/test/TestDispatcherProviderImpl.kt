package com.cramsan.framework.test

import com.cramsan.framework.core.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [DispatcherProvider] provides the dispatcher [ioDispatcher] to be used within test.
 * This allows tests to execute operation on the [ioDispatcher] but they are still bound to run within
 * the context of the unit test.
 */
@ExperimentalCoroutinesApi
class TestDispatcherProviderImpl(
    private val ioDispatcher: CoroutineDispatcher
) : DispatcherProvider {

    override fun ioDispatcher() = ioDispatcher
}
