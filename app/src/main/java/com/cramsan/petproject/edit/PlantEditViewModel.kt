package com.cramsan.petproject.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantEditViewModel(application: Application) : AndroidViewModel(application),
    KodeinAware {

    override val kodein by kodein(application)
    private val modelStorage: ModelStorageInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()

    private val observableIsComplete = MutableLiveData<Boolean>()
    private val observableIsLoading = MutableLiveData<Boolean>()

    fun isComplete(): LiveData<Boolean> {
        return observableIsComplete
    }

    fun isLoading(): LiveData<Boolean> {
        return observableIsLoading
    }

    fun savePlant(mainName: String, scientificName: String, family: String, toxicityForCats: ToxicityValue, toxicityForDogs: ToxicityValue) {
        eventLogger.log(Severity.INFO, classTag(), "savePlant")
        observableIsLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            modelStorage.insertPlant(Plant.PlantImp(-1, scientificName, ""))
            val plant = modelStorage.getPlant(scientificName)
            if (plant == null) {
                eventLogger.log(Severity.WARNING, classTag(), "Plant entry could not be created")
                return@launch
            }
            modelStorage.insertPlantMainName(PlantMainName.PlantMainNameImpl(-1, mainName, plant.id, "en"))
            modelStorage.insertPlantFamily(PlantFamily.PlantFamilyImpl(-1, family, plant.id, "en"))
            modelStorage.insertToxicity(Toxicity.ToxicityImpl(-1, plant.id, AnimalType.DOG.ordinal, toxicityForDogs.ordinal, ""))
            modelStorage.insertToxicity(Toxicity.ToxicityImpl(-1, plant.id, AnimalType.CAT.ordinal, toxicityForCats.ordinal, ""))
            eventLogger.log(Severity.INFO, classTag(), "Plant saved")
            viewModelScope.launch(Dispatchers.Main) {
                observableIsComplete.value = true
                observableIsLoading.value = true
            }
        }
    }
}
