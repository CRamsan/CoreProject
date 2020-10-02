package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope

/**
 * Base class that should handle running unit tests. This class will be implemented on each platform
 * to provide the right approach for each one of them.
 */
expect open class TestBase() {

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    val testCoroutineScope: CoroutineScope

    /**
     * We need to make sure that tests will be started with this function. Each platform will provide
     * the right configuration and rules to run unit tests. Ideally we should be able to use
     * [TestCoroutineScope] like we do in the Android implementation of [TestCoroutineRule], but we
     * need to wait for [TestCoroutineScope] to be available for common code.
     * https://github.com/Kotlin/kotlinx.coroutines/issues/1996
     */
    fun runBlockingTest(block: suspend () -> Unit)
}
