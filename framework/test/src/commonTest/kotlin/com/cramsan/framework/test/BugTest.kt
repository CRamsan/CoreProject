package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * This is a test for https://github.com/Kotlin/kotlinx.coroutines/issues/1204
 * There are some conditions that trigger runBlockingTest to complete before a child coroutine.
 * It seems to be marked as fixed although some people are reporting that it may still be happening.
 *
 */
class BugTest : TestBase() {

    @Test
    fun `Test for LiveData to be update in scope launch`() = runBlockingTest {
        val viewModel = TestViewModel(testCoroutineScope)
        viewModel.updateNumber()
        delay(100)

        assertEquals(100, viewModel.number)
    }
}

class TestViewModel(
    val testScope: CoroutineScope
) {
    var number = 0

    suspend fun updateNumber() {
        testScope.launch {
            delay(10)
            number = 100
        }
    }
}
