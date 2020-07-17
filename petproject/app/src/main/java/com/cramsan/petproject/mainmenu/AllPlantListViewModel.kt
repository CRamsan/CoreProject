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
import kotlin.properties.Delegates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.erased.instance

class AllPlantListViewModel(application: Application) : BaseViewModel(application),
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
    private val observableNextActivityCat = LiveEvent<Any>()
    private val observableNextActivityDog = LiveEvent<Any>()
    private val observableShowDataDownloaded = LiveEvent<Any>()
    private val observableShowIsDownloadedData = LiveEvent<Any>()
    private val observableStartDownload = LiveEvent<Any>()

    fun observablePlantListVisibility(): LiveData<Int> = observablePlantListVisibility
    fun observableMenuVisibility(): LiveData<Int> = observableMenuVisibility
    fun observableLoadingVisibility(): LiveData<Int> = observableLoadingVisibility
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableNextActivityCat(): LiveData<Any> = observableNextActivityCat
    fun observableNextActivityDog(): LiveData<Any> = observableNextActivityDog
    fun observableShowDataDownloaded(): LiveData<Any> = observableShowDataDownloaded
    fun observableShowIsDownloadedData(): LiveData<Any> = observableShowIsDownloadedData
    fun observableStartDownload(): LiveData<Any> = observableStartDownload

    private var inDownloadMode = false
    private var hasStarted = false
    var queryString: String by Delegates.observable("") {
        _, _, new -> searchPlants(new)
    }

    init {
        modelProvider.registerForCatalogEvents(this)
        observablePlants.value = emptyList()
    }

    fun tryStartDownload() {
        if (hasStarted) {
            return
        }
        hasStarted = true
        if (isCatalogReady()) {
            metricsClient.log(logTag, "start", mapOf("FromCache" to "True"))
        } else {
            metricsClient.log(logTag, "start", mapOf("FromCache" to "False"))
            observableStartDownload.value = 1
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
                observableShowDataDownloaded.postValue(1)
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
            observableShowIsDownloadedData.postValue(1)
            return
        }
        observableNextActivityCat.postValue(1)
    }

    private fun setInSearchMode(isSearchMode: Boolean) {
        if (isSearchMode) {
            observableMenuVisibility.postValue(View.GONE)
            observablePlantListVisibility.postValue(View.VISIBLE)
        } else {
            observableMenuVisibility.postValue(View.VISIBLE)
            observablePlantListVisibility.postValue(View.GONE)
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
