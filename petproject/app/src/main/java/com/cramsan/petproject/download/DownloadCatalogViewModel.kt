package com.cramsan.petproject.download

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.erased.instance

class DownloadCatalogViewModel(application: Application) : BaseViewModel(application),
    ModelProviderEventListenerInterface {

    private val modelProvider: ModelProviderInterface by instance()
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
        eventLogger.log(Severity.INFO, "DownloadCatalogViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis() / 1000L
        val isReady = modelProvider.isCatalogAvailable(unixTime)
        mutableIsDownloadComplete.postValue(isReady)
        return isReady
    }

    fun downloadCatalog() {
        eventLogger.log(Severity.INFO, "DownloadCatalogViewModel", "downloadCatalog")
        val unixTime = System.currentTimeMillis() / 1000L
        if (modelProvider.isCatalogAvailable(unixTime)) {
            mutableIsDownloadComplete.value = true
            return
        }
        mutableIsDownloadComplete.value = false
        GlobalScope.launch {
            downloadCatalogOnBackground()
        }
    }

    private suspend fun downloadCatalogOnBackground() = withContext(Dispatchers.IO) {
        val unixTime = System.currentTimeMillis() / 1000L
        modelProvider.downloadCatalog(unixTime)
        viewModelScope.launch {
            mutableIsDownloadComplete.value = true
        }
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            mutableIsDownloadComplete.value = !isReady
        }
    }
}
