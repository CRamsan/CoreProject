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
import com.cramsan.framework.userevents.logEvent
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base class for ViewModels that need to implement downloading the catalog.
 */
abstract class CatalogDownloadViewModel(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    protected val modelProvider: ModelProviderInterface,
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

    /**
     * Observable the represents the visibility status of a view or dialog.
     */
    fun observableDownloadingVisibility(): LiveData<Int> = observableDownloadingLoadingVisibility

    /**
     * Observable that emits an event to notify when the catalog data has been downloaded.
     */
    fun observableShowDataDownloaded(): LiveData<SimpleEvent> = observableShowDataDownloaded

    /**
     * Observable that emits an event to notify when the catalog data is being downloaded.
     */
    fun observableShowIsDownloadingData(): LiveData<SimpleEvent> = observableShowIsDownloadingData

    /**
     * Observable that emits an event to notify when the catalog data is starting to download.
     */
    fun observableStartDownload(): LiveData<SimpleEvent> = observableStartDownload

    private var inDownloadMode = false
    private var hasStarted = false

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    /**
     * Call this function to start a download of the catalog if it is needed.
     * If the local cache is valid, then no download operation will need to happen.
     */
    open fun tryStartDownload() {
        if (hasStarted) {
            return
        }
        hasStarted = true
        if (isCatalogReady()) {
            logEvent(logTag, "start", mapOf("FromCache" to "True"))
        } else {
            logEvent(logTag, "start", mapOf("FromCache" to "False"))
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

    /**
     * Return true if the catalog is downloaded and valid. False otherwise.
     */
    fun isCatalogReady(): Boolean {
        logI("AllPlantListViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis()
        return modelProvider.isCatalogAvailable(unixTime)
    }
}
