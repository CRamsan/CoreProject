package com.cramsan.framework.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The [ViewModel] provides the [viewModelScope] as a scope to launch coroutines on, the problem is that
 * during testing we need to be able to inject our own scopes so that we can ensure coroutines dispatched
 * into this scope are also completed.
 * https://stackoverflow.com/questions/62332403/how-to-inject-viewmodelscope-for-android-unit-test-with-kotlin-coroutines/62333592#62333592
 */
class AndroidViewModel(
    var testScope: CoroutineScope,
    val repository: Repository,
) : ViewModel() {

    val observableInt: MutableLiveData<Int> = MutableLiveData()

    suspend fun updateWithIODispatch() {
        observableInt.value = 0
        withContext(viewModelScope.coroutineContext) {
            observableInt.postValue(repository.getData())
        }
    }

    suspend fun updateWithIODispatchAndBlockingWait() {
        observableInt.value = 0
        withContext(viewModelScope.coroutineContext) {
            observableInt.postValue(repository.getDataBlocking())
        }
    }

    suspend fun updateWithScopeLaunch() {
        observableInt.value = 0
        viewModelScope.launch {
            observableInt.value = repository.getData()
        }
    }

    fun updateWithScopeLaunchAndBlockingWait() {
        observableInt.value = 0
        viewModelScope.launch {
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
