package com.cramsan.framework.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cramsan.framework.core.DispatcherProvider
import io.mockk.MockKAnnotations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
actual open class TestBase {
    // This is needed so that we can run APIs that interface with the different Android Loopers.
    // As a result all tasks that are dispatched to another looper, are executed instantaneously.
    // A prime example is MutableLiveData.postValue.
    // https://proandroiddev.com/how-to-unit-test-code-with-coroutines-50c1640f6bef
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) = testCoroutineRule.runBlockingTest { block() }

    @BeforeTest
    actual open fun setupTest() {
        MockKAnnotations.init(this)
    }

    /**
     * Reference to the Scope used to run the tests. This scope can be injected into
     * classes as well.
     */
    actual val testCoroutineScope: CoroutineScope
        get() = testCoroutineRule.testCoroutineScope

    val dispatcherProvider: DispatcherProvider = TestDispatcherProviderImpl(testCoroutineRule)
}
