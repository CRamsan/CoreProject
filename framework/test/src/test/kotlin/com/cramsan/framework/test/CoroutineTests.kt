package com.cramsan.framework.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTests : TestBase() {

    lateinit var commonCoroutineTests: CommonCoroutineTests

    @BeforeTest
    fun setUp() {
        commonCoroutineTests = CommonCoroutineTests()
        commonCoroutineTests.setUp(RepositoryImpl())
    }

    @Test
    fun `Test simple assert`() {
        commonCoroutineTests.`Test simple assert`()
    }

    @ExperimentalTime
    @Test
    fun `Test delays are executed instantly`() {
        commonCoroutineTests.`Test delays are executed instantly`()
    }

    @Test
    fun `Test for update in suspending function`() {
        commonCoroutineTests.`Test for update in suspending function`()
    }

    @Test
    fun `Test for update in suspending function and blocking wait`() {
        commonCoroutineTests.`Test for update in suspending function and blocking wait`()
    }

    @Test
    fun `Test for updated with IO dispatch`() {
        commonCoroutineTests.`Test for updated with IO dispatch`()
    }

    @Test
    fun `Test for updated with IO dispatch and blocking wait`() {
        commonCoroutineTests.`Test for updated with IO dispatch and blocking wait`()
    }

    @Test
    fun `Test for update in scope launch`() {
        commonCoroutineTests.`Test for update in scope launch`()
    }

    @Test
    fun `Test for update in scope launch and blocking wait`() {
        commonCoroutineTests.`Test for update in scope launch and blocking wait`()
    }
}
