package com.cramsan.petproject.download

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

open class DownloadCatalogViewModel(application: Application) : AndroidViewModel(application), KodeinAware,
    ModelProviderEventListenerInterface {

    override val kodein by kodein(application)
    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()

    private val observableLoading = MutableLiveData<Boolean>()

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    override fun onCleared() {
        super.onCleared()
        modelProvider.deregisterForCatalogEvents(this)
    }

    fun isCatalogReady(): Boolean {
        eventLogger.log(Severity.INFO, "DownloadCatalogViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis() / 1000L
        return modelProvider.isCatalogAvailable(unixTime)
    }

    fun downloadCatalog() {
        eventLogger.log(Severity.INFO, "DownloadCatalogViewModel", "reloadPlants")
        val unixTime = System.currentTimeMillis() / 1000L
        if (modelProvider.isCatalogAvailable(unixTime)) {
            observableLoading.value = false
            return
        }
        observableLoading.value = true
        viewModelScope.launch {
            downloadCatalogOnBackground()
        }
    }

    fun observableLoading(): LiveData<Boolean> {
        return observableLoading
    }

    private suspend fun downloadCatalogOnBackground() = withContext(Dispatchers.IO) {
        val unixTime = System.currentTimeMillis() / 1000L
        modelProvider.downloadCatalog(unixTime)
        viewModelScope.launch {
            observableLoading.value = false
        }
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            observableLoading.value = !isReady
        }
    }
}
