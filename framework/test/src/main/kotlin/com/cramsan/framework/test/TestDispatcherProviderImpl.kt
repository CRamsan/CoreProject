package com.cramsan.framework.test

import com.cramsan.framework.core.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [DispatcherProvider] provides the dispatcher from the [testCoroutineRule] to [ioDispatcher].
 * This allows tests to execute operation on the [ioDispatcher] but they are still bound to run within
 * the context of the unit test.
 */
@ExperimentalCoroutinesApi
class TestDispatcherProviderImpl(
    private val testCoroutineRule: TestCoroutineRule
) : DispatcherProvider {

    override fun ioDispatcher() = testCoroutineRule.testCoroutineDispatcher
}
