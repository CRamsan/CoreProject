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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.erased.instance
import kotlin.properties.Delegates

class PlantListViewModel(application: Application) : BaseViewModel(application),
    ModelProviderEventListenerInterface {

    private val modelProvider: ModelProviderInterface by instance()
    override val logTag: String
        get() = "PlantListViewModel"

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableLoading = MutableLiveData<Boolean>()
    private val observableAnimalType = MutableLiveData<AnimalType>()
    private val observableLoadingVisibility = MutableLiveData<Int>(View.VISIBLE)

    fun observableLoadingVisibility(): LiveData<Int> = observableLoadingVisibility
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableLoading(): LiveData<Boolean> = observableLoading
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
            observableLoading.value = isReady
        }
    }

    fun setAnimalType(animalType: AnimalType) {
        observableAnimalType.value = animalType
        viewModelScope.launch {
            filterPlants(queryString)
        }
    }

    private fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "PlantListViewModel", "searchPlants")
        observableLoadingVisibility.postValue(View.VISIBLE)
        viewModelScope.launch {
            filterPlants(query)
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val animalType = observableAnimalType.value
        if (animalType == null) {
            eventLogger.log(Severity.WARNING, "PlantListViewModel", "Unable to filterPlants. AnimalType is null")
            return@withContext
        }

        val plants: List<PresentablePlant>? = if (query.isEmpty()) {
            modelProvider.getPlantsWithToxicity(animalType, "en")
        } else {
            modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en") ?: return@withContext
        }

        viewModelScope.launch {
            observableLoadingVisibility.postValue(View.GONE)
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }
}
