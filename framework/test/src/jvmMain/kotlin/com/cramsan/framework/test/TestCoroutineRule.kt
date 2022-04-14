package com.cramsan.framework.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * This is a copy-paste of the Android's version of this rule. We are waiting to be able to share
 * JVM/Android source sets so we can keep a single file. For now please do not modify this file and
 * instead update the Android one and then copy-paste that file here.
 */
@ExperimentalCoroutinesApi
class TestCoroutineRule : AfterEachCallback, BeforeEachCallback {

    val testCoroutineDispatcher = StandardTestDispatcher()
    val testCoroutineScope = TestScope(testCoroutineDispatcher)

    fun runBlockingTest(block: suspend TestScope.() -> Unit) = testCoroutineScope.runTest { block() }

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
