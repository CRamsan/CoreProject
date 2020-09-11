package com.cramsan.framework.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.hours

@ExperimentalCoroutinesApi
class CommonCoroutineTests : TestBase() {

    lateinit var viewModel: SimpleViewModel
    lateinit var repository: Repository

    @BeforeTest
    fun setUp() {
        repository = Repository()
        viewModel = SimpleViewModel(testCoroutineScope, repository)
    }

    @Test
    fun `Test simple assert`() = runBlockingTest {
        assertTrue(true)
        assertFalse(false)
        assertEquals("word", "word")
        assertNull(null)
        assertNotEquals("word", null)
    }

    @ExperimentalTime
    @Test
    fun `Test delays are executed instantly`() = runBlockingTest {
        // This method should run instantly
        delay(1.hours)
    }

    @Test
    fun `Test for update in suspending function`() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithCoroutine()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun `Test for update in suspending function and blocking wait`() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithCoroutineAndBlockingWait()

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun `Test for updated with IO dispatch`() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithIODispatch()
        delay(1000)

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun `Test for updated with IO dispatch and blocking wait`() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithIODispatchAndBlockingWait()
        delay(1000)

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun `Test for update in scope launch`() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithScopeLaunch()
        delay(1000)

        assertEquals(100, viewModel.observableInt.value)
    }

    @Test
    fun `Test for update in scope launch and blocking wait`() = runBlockingTest {
        assertEquals(0, viewModel.observableInt.value)

        viewModel.updateWithScopeLaunchAndBlockingWait()

        assertEquals(100, viewModel.observableInt.value)
    }
}