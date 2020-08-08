package com.cramsan.petproject.mainmenu

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.BaseViewModel
import com.cramsan.petproject.base.LiveEvent
import com.cramsan.petproject.base.SimpleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.erased.instance
import kotlin.properties.Delegates

class AllPlantListViewModel(application: Application) :
    BaseViewModel(application),
    ModelProviderEventListenerInterface {

    private val modelProvider: ModelProviderInterface by instance()
    override val logTag: String
        get() = "AllPlantListViewModel"

    // State
    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observablePlantListVisibility = MutableLiveData<Int>(View.VISIBLE)
    private val observableMenuVisibility = MutableLiveData<Int>(View.VISIBLE)
    private val observableLoadingVisibility = MutableLiveData<Int>(View.GONE)

    // Events
    private val observableNextActivityCat = LiveEvent<SimpleEvent>()
    private val observableNextActivityDog = LiveEvent<SimpleEvent>()
    private val observableShowDataDownloaded = LiveEvent<SimpleEvent>()
    private val observableShowIsDownloadedData = LiveEvent<SimpleEvent>()
    private val observableStartDownload = LiveEvent<SimpleEvent>()

    fun observablePlantListVisibility(): LiveData<Int> = observablePlantListVisibility
    fun observableMenuVisibility(): LiveData<Int> = observableMenuVisibility
    fun observableLoadingVisibility(): LiveData<Int> = observableLoadingVisibility
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableNextActivityCat(): LiveData<SimpleEvent> = observableNextActivityCat
    fun observableNextActivityDog(): LiveData<SimpleEvent> = observableNextActivityDog
    fun observableShowDataDownloaded(): LiveData<SimpleEvent> = observableShowDataDownloaded
    fun observableShowIsDownloadedData(): LiveData<SimpleEvent> = observableShowIsDownloadedData
    fun observableStartDownload(): LiveData<SimpleEvent> = observableStartDownload

    private var inDownloadMode = false
    private var hasStarted = false
    var queryString: String by Delegates.observable("") {
        _, _, new ->
        searchPlants(new)
    }

    init {
        modelProvider.registerForCatalogEvents(this)
        observablePlants.value = emptyList()
    }

    fun tryStartDownload() {
        // Restore the state of the plant list back to all.
        viewModelScope.launch(Dispatchers.IO) {
            modelProvider.getPlantsWithToxicity(AnimalType.ALL, "en")
        }
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

    fun goToCats(view: View) {
        goToNextActivity(AnimalType.CAT)
    }

    fun goToDogs(view: View) {
        goToNextActivity(AnimalType.DOG)
    }

    private fun goToNextActivity(animalType: AnimalType) {
        if (!isCatalogReady()) {
            observableShowIsDownloadedData.value = SimpleEvent()
            return
        }
        observableNextActivityCat.value = SimpleEvent()
    }

    private fun setInSearchMode(isSearchMode: Boolean) {
        if (isSearchMode) {
            observableMenuVisibility.value = View.GONE
            observablePlantListVisibility.value = View.VISIBLE
        } else {
            observableMenuVisibility.value = View.VISIBLE
            observablePlantListVisibility.value = View.GONE
        }
    }

    private fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "searchPlants")
        observableLoadingVisibility.value = View.VISIBLE

        if (query.isEmpty()) {
            setInSearchMode(false)
            observableLoadingVisibility.value = View.GONE
            return
        }

        setInSearchMode(true)
        viewModelScope.launch {
            filterPlants(query)
        }
    }

    fun isCatalogReady(): Boolean {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis() / 1000L
        return modelProvider.isCatalogAvailable(unixTime)
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicityFiltered(AnimalType.ALL, query, "en") ?: return@withContext
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observableLoadingVisibility.value = View.GONE
            observablePlants.value = plants
        }
    }
}
