package com.cramsan.framework.test

import com.cramsan.framework.core.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TestDispatcherProviderImpl(
    private val testCoroutineRule: TestCoroutineRule
) : DispatcherProvider {

    override fun ioDispatcher() = testCoroutineRule.testCoroutineDispatcher
}
