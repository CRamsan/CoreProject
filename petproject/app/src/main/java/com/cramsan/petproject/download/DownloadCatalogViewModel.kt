package com.cramsan.petproject.download

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

class DownloadCatalogViewModel @ViewModelInject constructor(
    application: Application,
    eventLogger: EventLoggerInterface,
    metricsClient: MetricsInterface,
    threadUtil: ThreadUtilInterface,
    val modelProvider: ModelProviderInterface,
    val IODispatcher: CoroutineDispatcher
) :
    BaseViewModel(application, eventLogger, metricsClient, threadUtil),
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
        eventLogger.log(Severity.INFO, "DownloadCatalogViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis() / 1000L
        val isReady = modelProvider.isCatalogAvailable(unixTime)
        mutableIsDownloadComplete.value = isReady
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
        viewModelScope.launch {
            if (checkIfCatalogIsAvailable()) {
                dispatchDownloadCatalogOnBackground()
            } else {
                downloadCatalogOnBackground()
            }
        }
    }

    private suspend fun checkIfCatalogIsAvailable(): Boolean = withContext(IODispatcher) {
        return@withContext modelProvider.getPlantsWithToxicity(AnimalType.CAT, "en").isNotEmpty()
    }

    private suspend fun dispatchDownloadCatalogOnBackground() {
        mutableIsDownloadComplete.postValue(true)
        val unixTime = System.currentTimeMillis() / 1000L
        GlobalScope.launch(IODispatcher) {
            modelProvider.downloadCatalog(unixTime)
        }
    }

    private suspend fun downloadCatalogOnBackground() = withContext(IODispatcher) {
        val unixTime = System.currentTimeMillis() / 1000L
        GlobalScope.launch {
            modelProvider.downloadCatalog(unixTime)
        }.join()
        mutableIsDownloadComplete.postValue(true)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            mutableIsDownloadComplete.value = !isReady
        }
    }
}
