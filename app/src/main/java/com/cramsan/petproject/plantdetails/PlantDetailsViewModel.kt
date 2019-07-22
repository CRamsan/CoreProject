package com.cramsan.petproject.plantdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantDetailsViewModel : ViewModel() {

    private val modelStore = CoreFrameworkAPI.modelProvider

    private val observablePlant = MutableLiveData<Plant>()
    private val observablePlantMetadata = MutableLiveData<PlantMetadata>()

    fun reloadPlant(animalType: AnimalType, plantId: Int) {
        viewModelScope.launch {
            loadPlant(animalType, plantId)
        }
    }

    fun getPlant(): LiveData<Plant> {
        return observablePlant
    }

    fun getPlantMetadata(): LiveData<PlantMetadata> {
        return observablePlantMetadata
    }

    private suspend fun loadPlant(animalType: AnimalType, plantId: Int) = withContext(Dispatchers.IO) {
        val plant = modelStore.getPlant(animalType, plantId, "en")
        val plantMetadata = modelStore.getPlantMetadata(AnimalType.CAT, plantId, "en")
        viewModelScope.launch {
            observablePlant.value = plant
            observablePlantMetadata.value = plantMetadata
        }
    }
}
