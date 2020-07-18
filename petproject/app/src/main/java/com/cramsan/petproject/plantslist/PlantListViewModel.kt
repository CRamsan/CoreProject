package com.cramsan.petproject.plantslist

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
import kotlin.properties.Delegates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.erased.instance

class PlantListViewModel(application: Application) : BaseViewModel(application),
    ModelProviderEventListenerInterface {

    private val modelProvider: ModelProviderInterface by instance()
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

    var queryString: String by Delegates.observable("") {
        _, _, new -> searchPlants(new)
    }

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    override fun onCleared() {
        modelProvider.deregisterForCatalogEvents(this)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            setSearchMode(!isReady)
        }
    }

    fun setAnimalType(animalType: AnimalType) {
        observableAnimalType.value = animalType
        viewModelScope.launch {
            filterPlants(queryString)
        }
    }

    private fun setSearchMode(isSearchMode: Boolean) {
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
            setSearchMode(true)
        }

        if (animalType == null) {
            eventLogger.log(Severity.WARNING, "PlantListViewModel", "Unable to filterPlants. AnimalType is null")
            viewModelScope.launch {
                setSearchMode(true)
            }
            return@withContext
        }

        val plants: List<PresentablePlant>? = if (query.isEmpty()) {
            modelProvider.getPlantsWithToxicity(animalType, "en")
        } else {
            modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en") ?: return@withContext
        }

        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            setSearchMode(false)
            observablePlants.value = plants
        }
    }
}
