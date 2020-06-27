package com.cramsan.petproject.plantslist

import android.app.Application
import androidx.lifecycle.*
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantListViewModel(application: Application) : AndroidViewModel(application), KodeinAware,
    ModelProviderEventListenerInterface {

    override val kodein by kodein(application)
    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()
    private val threadUtil: ThreadUtilInterface by instance()

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableLoading = MutableLiveData<Boolean>()
    private val observableAnimalType = MutableLiveData<AnimalType>()
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableLoading(): LiveData<Boolean> = observableLoading
    fun observableAnimalType(): LiveData<AnimalType> = observableAnimalType

    var searchQuery: String? = null

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    override fun onCleared() {
        modelProvider.deregisterForCatalogEvents(this)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            if (isReady) {
                loadPlants()
            }
            observableLoading.value = isReady
        }
    }

    fun setAnimalType(animalType: AnimalType) {
        observableAnimalType.value = animalType
        viewModelScope.launch {
            loadPlants()
        }
    }

    fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "PlantListViewModel", "searchPlants")
        searchQuery = query
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadPlants()
            } else {
                filterPlants(query)
            }
        }
    }

    private suspend fun loadPlants() = withContext(Dispatchers.IO) {
        val animalType = observableAnimalType.value
        if (animalType == null) {
            eventLogger.log(Severity.WARNING, "PlantListViewModel", "Unable to loadPlants. AnimalType is null")
            return@withContext
        }

        val plants = modelProvider.getPlantsWithToxicity(animalType, "en")
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observableLoading.value = false
            observablePlants.value = plants
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val animalType = observableAnimalType.value
        if (animalType == null) {
            eventLogger.log(Severity.WARNING, "PlantListViewModel", "Unable to filterPlants. AnimalType is null")
            return@withContext
        }

        val plants = modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en") ?: return@withContext
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }
}
