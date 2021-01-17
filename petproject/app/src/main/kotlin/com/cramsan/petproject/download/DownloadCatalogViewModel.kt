package com.cramsan.petproject.download

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logI
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadCatalogViewModel @ViewModelInject constructor(
    application: Application,
    val modelProvider: ModelProviderInterface,
    val IODispatcher: CoroutineDispatcher,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle,
) :
    BaseViewModel(application, dispatcherProvider, savedStateHandle),
    ModelProviderEventListenerInterface {

    override val logTag: String
        get() = "DownloadCatalogViewModel"

    private val mutableIsDownloadComplete = MutableLiveData<Boolean>()
    val observableIsDownloadComplete: LiveData<Boolean> = mutableIsDownloadComplete

    init {
        modelProvider.registerForCatalogEvents(this)
        mutableIsDownloadComplete.value = false
    }

    override fun onCleared() {
        super.onCleared()
        modelProvider.deregisterForCatalogEvents(this)
    }

    fun isCatalogReady(): Boolean {
        logI("DownloadCatalogViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis()
        val isReady = modelProvider.isCatalogAvailable(unixTime)
        mutableIsDownloadComplete.value = isReady
        return isReady
    }

    fun downloadCatalog() {
        logI("DownloadCatalogViewModel", "downloadCatalog")
        val unixTime = System.currentTimeMillis()
        if (modelProvider.isCatalogAvailable(unixTime)) {
            mutableIsDownloadComplete.value = true
            return
        }
        mutableIsDownloadComplete.value = false
        ioScope.launch {
            downloadCatalogOnBackground()
        }
    }

    private suspend fun downloadCatalogOnBackground() = withContext(IODispatcher) {
        val unixTime = System.currentTimeMillis()
        ioScope.launch {
            modelProvider.downloadCatalog(unixTime)
        }.join()
        mutableIsDownloadComplete.postValue(true)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        ioScope.launch {
            mutableIsDownloadComplete.value = !isReady
        }
    }
}
