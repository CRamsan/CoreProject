package com.cramsan.petproject.base

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.instance

abstract class CatalogDownloadViewModel(application: Application) :
    BaseViewModel(application),
    ModelProviderEventListenerInterface {

    protected val modelProvider: ModelProviderInterface by instance()
    override val logTag: String
        get() = "CatalogDownloadViewModel"

    // State
    private val observableDownloadingLoadingVisibility = MutableLiveData<Int>(View.GONE)

    // Events
    protected val observableShowDataDownloaded = LiveEvent<SimpleEvent>()
    protected val observableShowIsDownloadingData = LiveEvent<SimpleEvent>()
    protected val observableStartDownload = LiveEvent<SimpleEvent>()

    fun observableDownloadingVisibility(): LiveData<Int> = observableDownloadingLoadingVisibility
    fun observableShowDataDownloaded(): LiveData<SimpleEvent> = observableShowDataDownloaded
    fun observableShowIsDownloadingData(): LiveData<SimpleEvent> = observableShowIsDownloadingData
    fun observableStartDownload(): LiveData<SimpleEvent> = observableStartDownload

    private var inDownloadMode = false
    private var hasStarted = false

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    open fun tryStartDownload() {
        if (hasStarted) {
            return
        }
        hasStarted = true
        if (isCatalogReady()) {
            metricsClient.log(logTag, "start", mapOf("FromCache" to "True"))
        } else {
            metricsClient.log(logTag, "start", mapOf("FromCache" to "False"))
            observableStartDownload.value = SimpleEvent()
            inDownloadMode = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        modelProvider.deregisterForCatalogEvents(this)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            if (!isReady) {
                return@launch
            }

            if (inDownloadMode) {
                observableShowDataDownloaded.value = SimpleEvent()
            }
            launch(Dispatchers.IO) {
                modelProvider.getPlantsWithToxicity(AnimalType.ALL, "en")
            }
        }
    }

    fun isCatalogReady(): Boolean {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis() / 1000L
        return modelProvider.isCatalogAvailable(unixTime)
    }
}
