package com.cramsan.petproject.plantdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
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
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "reloadPlant")
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
        val plantMetadata = modelStore.getPlantMetadata(animalType, plantId, "en")
        viewModelScope.launch {
            observablePlant.value = plant
            observablePlantMetadata.value = plantMetadata
        }
    }
}
