package com.cramsan.framework.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Set of test for common coroutine scenarios in common code.
 */
@ExperimentalCoroutinesApi
class SimpleViewModel(
    var testScope: CoroutineScope,
    val repository: Repository
) {

    val observableInt = MutableStateFlow(0)

    suspend fun updateWithIODispatch() {
        observableInt.value = 0
        withContext(testScope.coroutineContext) {
            observableInt.value = repository.getData()
        }
    }

    suspend fun updateWithIODispatchAndBlockingWait() {
        observableInt.value = 0
        withContext(testScope.coroutineContext) {
            observableInt.value = repository.getDataBlocking()
        }
    }

    suspend fun updateWithScopeLaunch() {
        observableInt.value = 0
        testScope.launch {
            observableInt.value = repository.getData()
        }
    }

    fun updateWithScopeLaunchAndBlockingWait() {
        observableInt.value = 0
        testScope.launch {
            observableInt.value = repository.getDataBlocking()
        }
    }

    suspend fun updateWithCoroutine() {
        observableInt.value = 0
        observableInt.value = repository.getData()
    }

    fun updateWithCoroutineAndBlockingWait() {
        observableInt.value = 0
        observableInt.value = repository.getDataBlocking()
    }
}
