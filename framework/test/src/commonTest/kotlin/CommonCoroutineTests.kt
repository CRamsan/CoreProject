package com.cramsan.framework.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CommonCoroutineTests : TestBase() {

    lateinit var viewModel: SimpleViewModel
    lateinit var repository: Repository

    override fun setupTest() {
        this.repository = Repository()
        this.viewModel = SimpleViewModel(testCoroutineScope, repository)
    }

    @Test
    fun Test_simple_assert() = runBlockingTest {
        assertTrue(true)
        assertFalse(false)
        assertEquals("word", "word")
        assertNull(null)
        assertNotEquals<String?>("word", null)
    }

    @Test
    fun Test_delays_are_executed_instantly() = runBlockingTest {
        // This method should run instantly
        // When upgrading to Kotlin 1.5 we can use the new Duration.hours API.
        delay((1000 * 60 * 60).toLong())
    }

    @Test
    fun Test_for_update_in_suspending_function() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithCoroutine()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun Test_for_update_in_suspending_function_and_blocking_wait() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithCoroutineAndBlockingWait()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun Test_for_updated_with_IO_dispatch() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithIODispatch()
        advanceUntilIdle()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun Test_for_updated_with_IO_dispatch_and_blocking_wait() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithIODispatchAndBlockingWait()
        advanceUntilIdle()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun Test_for_update_in_scope_launch() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithScopeLaunch()
        advanceUntilIdle()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun Test_for_update_in_scope_launch_and_blocking_wait() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithScopeLaunchAndBlockingWait()

        advanceUntilIdle()

        assertEquals(100, viewModel.observableInt.value)
    }
}
