package com.cramsan.framework.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * This rule is used in Android to configure the main dispatcher and set the dispatcher to a new
 * [TestCoroutineScope].
 * Source: https://proandroiddev.com/how-to-unit-test-code-with-coroutines-50c1640f6bef
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
