package com.cramsan.framework.utils.dispatcher

import com.cramsan.framework.test.TestCoroutineRule

class TestDispatcherProviderImpl(
    private val testCoroutineRule: TestCoroutineRule
) : DispatcherProvider {

    override fun mainDispatcher() = testCoroutineRule.testCoroutineDispatcher

    override fun ioDispatcher() = testCoroutineRule.testCoroutineDispatcher
}
