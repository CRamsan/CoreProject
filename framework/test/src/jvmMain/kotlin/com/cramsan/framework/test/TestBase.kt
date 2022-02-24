package com.cramsan.framework.test

import io.mockk.MockKAnnotations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Rule
import kotlin.test.BeforeTest

/**
 * This is a copy-paste of the Android version of this file. We are waiting for support of code sharing
 * between Android and JVM so we can finally use a single file.
 */
@OptIn(ExperimentalCoroutinesApi::class)
actual abstract class TestBase {

    @get:Rule
    var testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    actual fun runBlockingTest(block: suspend TestScope.() -> Unit) = testCoroutineRule.runBlockingTest { block() }

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    actual val testCoroutineScope: CoroutineScope
        get() = testCoroutineRule.testCoroutineScope

    @BeforeTest
    fun internalSetupTest() {
        MockKAnnotations.init(this)
        setupTest()
    }

    actual abstract fun setupTest()
}
