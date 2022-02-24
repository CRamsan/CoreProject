package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
actual abstract class TestBase {

    val testCoroutineDispatcher = StandardTestDispatcher()
    private val _testCoroutineScope = TestScope(testCoroutineDispatcher)

    actual fun runBlockingTest(block: suspend TestScope.() -> Unit): dynamic =
        _testCoroutineScope.runTest { block() }

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    actual val testCoroutineScope: CoroutineScope = _testCoroutineScope

    @BeforeTest
    fun internalSetupTest() {
        setupTest()
    }

    actual abstract fun setupTest()
}
