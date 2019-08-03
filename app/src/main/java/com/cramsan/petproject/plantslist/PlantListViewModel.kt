package com.cramsan.petproject.plantslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PresentablePlant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantListViewModel : ViewModel() {

    private val modelStore = CoreFrameworkAPI.modelProvider

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()

    fun reloadPlants(animalType: AnimalType) {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "reloadPlants")
        viewModelScope.launch {
            loadPlants(animalType)
        }
    }

    fun searchPlants(query: String, animalType: AnimalType) {
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "searchPlants")
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadPlants(animalType)
            } else {
                filterPlants(query, animalType)
            }
        }
    }

    fun observablePlants(): LiveData<List<PresentablePlant>> {
        return observablePlants
    }

    private suspend fun loadPlants(animalType: AnimalType) = withContext(Dispatchers.IO)  {
        val plants = modelStore.getPlantsWithToxicity(animalType, "en")
        viewModelScope.launch {
            CoreFrameworkAPI.threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }

    private suspend fun filterPlants(query: String, animalType: AnimalType) = withContext(Dispatchers.IO)  {
        val plants = modelStore.getPlantsWithToxicityFiltered(animalType, query, "en") ?: return@withContext
        viewModelScope.launch {
            CoreFrameworkAPI.threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }
}
