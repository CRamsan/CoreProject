package com.cramsan.framework.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * This is a copy-paste of the Android's version of this rule. We are waiting to be able to share
 * JVM/Android source sets so we can keep a single file. For now please do not modify this file and
 * instead update the Android one and then copy-paste that file here.
 */
@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    val testCoroutineDispatcher = StandardTestDispatcher()
    val testCoroutineScope = TestScope(testCoroutineDispatcher)

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)

            base.evaluate()

            Dispatchers.resetMain()
        }
    }

    fun runBlockingTest(block: suspend TestScope.() -> Unit) = testCoroutineScope.runTest { block() }
}
