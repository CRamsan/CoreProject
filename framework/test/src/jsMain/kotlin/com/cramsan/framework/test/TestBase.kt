package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise
import kotlin.test.BeforeTest

actual open class TestBase {

    actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit): dynamic = testCoroutineScope.promise { block() }

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    actual val testCoroutineScope: CoroutineScope
        get() = TODO("Not yet implemented")

    @BeforeTest
    actual open fun setupTest() { }
}