package com.cramsan.petproject.plantslist

import android.app.Application
import android.view.View
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.CatalogDownloadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantListViewModel @ViewModelInject constructor(
    application: Application,
    eventLogger: EventLoggerInterface,
    metricsClient: MetricsInterface,
    threadUtil: ThreadUtilInterface,
    modelProvider: ModelProviderInterface,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    CatalogDownloadViewModel(application, eventLogger, metricsClient, threadUtil, modelProvider) {

    override val logTag: String
        get() = "PlantListViewModel"

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableAnimalType = MutableLiveData<AnimalType>()
    private val observableLoadingVisibility = MutableLiveData<Int>(View.GONE)
    private val observablePlantListVisibility = MutableLiveData<Int>(View.GONE)

    fun observableLoadingVisibility(): LiveData<Int> = observableLoadingVisibility
    fun observablePlantListVisibility(): LiveData<Int> = observablePlantListVisibility
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableAnimalType(): LiveData<AnimalType> = observableAnimalType

    var queryString = MutableStateFlow("")

    init {
        queryString.onEach {
            searchPlants(it)
        }.launchIn(viewModelScope)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            setLoadingMode(!isReady)
        }
    }

    fun setAnimalType(animalType: AnimalType) {
        observableAnimalType.value = animalType
        viewModelScope.launch {
            filterPlants(queryString.value)
        }
    }

    fun setLoadingMode(isSearchMode: Boolean) {
        if (isSearchMode) {
            observableLoadingVisibility.value = View.VISIBLE
            observablePlantListVisibility.postValue(View.GONE)
        } else {
            observableLoadingVisibility.value = View.GONE
            observablePlantListVisibility.postValue(View.VISIBLE)
        }
    }

    private fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "PlantListViewModel", "searchPlants")
        viewModelScope.launch {
            filterPlants(query)
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val animalType = observableAnimalType.value
        viewModelScope.launch {
            setLoadingMode(true)
        }

        if (animalType == null) {
            eventLogger.log(
                Severity.WARNING,
                "PlantListViewModel",
                "Unable to filterPlants. AnimalType is null"
            )
            viewModelScope.launch {
                setLoadingMode(true)
            }
            return@withContext
        }

        val plants: List<PresentablePlant>? = if (query.isEmpty()) {
            modelProvider.getPlantsWithToxicity(animalType, "en")
        } else {
            modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en")
                ?: return@withContext
        }

        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            setLoadingMode(false)
            observablePlants.value = plants
        }
    }
}
