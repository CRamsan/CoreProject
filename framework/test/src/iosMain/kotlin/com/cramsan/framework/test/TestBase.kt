package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual open class TestBase {

    actual fun runBlockingTest(block: suspend () -> Unit) = runBlocking { block() }

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    actual val testCoroutineScope: CoroutineScope
        get() = TODO("Not yet implemented")
}
