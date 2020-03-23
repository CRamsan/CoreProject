package com.cramsan.petproject.plantdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantDetailsViewModel(application: Application) : AndroidViewModel(application), KodeinAware {

    override val kodein by kodein(application)
    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()

    private val observablePlant = MutableLiveData<Plant?>()
    private val observablePlantMetadata = MutableLiveData<PlantMetadata?>()

    fun reloadPlant(animalType: AnimalType, plantId: Int) {
        eventLogger.log(Severity.INFO, "PlantDetailsViewModel", "reloadPlant")
        viewModelScope.launch {
            loadPlant(animalType, plantId)
        }
    }

    fun getPlant(): LiveData<Plant?> {
        return observablePlant
    }

    fun getPlantMetadata(): LiveData<PlantMetadata?> {
        return observablePlantMetadata
    }

    private suspend fun loadPlant(animalType: AnimalType, plantId: Int) = withContext(Dispatchers.IO) {
        val plant = modelProvider.getPlant(animalType, plantId, "en")
        val plantMetadata = modelProvider.getPlantMetadata(animalType, plantId, "en")
        viewModelScope.launch {
            observablePlant.value = plant
            observablePlantMetadata.value = plantMetadata
        }
    }
}
