package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

/**
 * This is a compy-paste of the Android version of this file. We are waiting for support of code sharing
 * between Android and JVM so we can finally use a single file.
 */
@ExperimentalCoroutinesApi
actual open class TestBase {

    @get:Rule
    var testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) = testCoroutineRule.runBlockingTest { block() }

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    actual val testCoroutineScope: CoroutineScope
        get() = testCoroutineRule.testCoroutineScope
}
