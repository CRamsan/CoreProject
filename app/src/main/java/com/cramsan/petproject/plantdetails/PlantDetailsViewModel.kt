package com.cramsan.petproject.plantdetails

import android.app.Application
import androidx.lifecycle.*
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance
import org.kodein.di.android.kodein

class PlantDetailsViewModel(application: Application) : AndroidViewModel(application), KodeinAware {

    override val kodein by kodein(application)
    private val modelStore: ModelStorageInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()

    private val observablePlant = MutableLiveData<Plant>()
    private val observablePlantMetadata = MutableLiveData<PlantMetadata>()

    fun reloadPlant(animalType: AnimalType, plantId: Int) {
        eventLogger.log(Severity.INFO, classTag(), "reloadPlant")
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
