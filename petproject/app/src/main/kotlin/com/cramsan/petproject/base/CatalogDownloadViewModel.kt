package com.cramsan.petproject.base

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.LiveEvent
import com.cramsan.framework.core.SimpleEvent
import com.cramsan.framework.logging.logI
import com.cramsan.framework.metrics.logMetric
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class CatalogDownloadViewModel(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    val modelProvider: ModelProviderInterface,
    savedStateHandle: SavedStateHandle,
) :
    BaseViewModel(application, dispatcherProvider, savedStateHandle),
    ModelProviderEventListenerInterface {

    // State
    private val observableDownloadingLoadingVisibility = MutableLiveData<Int>(View.GONE)

    // Events
    @Suppress("DEPRECATION")
    protected val observableShowDataDownloaded = LiveEvent<SimpleEvent>()
    @Suppress("DEPRECATION")
    protected val observableShowIsDownloadingData = LiveEvent<SimpleEvent>()
    @Suppress("DEPRECATION")
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
            logMetric(logTag, "start", mapOf("FromCache" to "True"))
        } else {
            logMetric(logTag, "start", mapOf("FromCache" to "False"))
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
        logI("AllPlantListViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis()
        return modelProvider.isCatalogAvailable(unixTime)
    }
}
