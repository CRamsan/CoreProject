package com.cramsan.petproject.plantdetails

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.erased.instance

class PlantDetailsViewModel(application: Application) : BaseViewModel(application) {

    private val modelProvider: ModelProviderInterface by instance()
    override val logTag: String
        get() = "PlantDetailsViewModel"

    val observablePlantName = MutableLiveData<String>()
    val observablePlantScientificName = MutableLiveData<String>()
    val observablePlantFamily = MutableLiveData<String>()
    val observablePlantImageSource = MutableLiveData<String>()
    val observablePlantCommonNames = MutableLiveData<String>()
    val observablePlantDescription = MutableLiveData<String>()
    val observableSource = MutableLiveData<String>()
    val observableDangerousText = MutableLiveData<Int>(R.string.string_empty)
    val observableDangerousColor = MutableLiveData<Int>(R.color.colorUndetermined)
    val observablePlantCommonNamesVisibility = Transformations.map(observablePlantCommonNames) {
            commonName -> if (commonName.isEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    } }

    fun reloadPlant(animalType: AnimalType, plantId: Int) {
        eventLogger.log(Severity.INFO, "PlantDetailsViewModel", "reloadPlant")
        viewModelScope.launch {
            loadPlant(animalType, plantId)
        }
    }

    private suspend fun loadPlant(animalType: AnimalType, plantId: Int) = withContext(Dispatchers.IO) {
        val plant = modelProvider.getPlant(animalType, plantId, "en")
        val plantMetadata = modelProvider.getPlantMetadata(animalType, plantId, "en")
        if (plant == null || plantMetadata == null) {
            // TODO: Show error message
            TODO()
        }
        viewModelScope.launch {
            observablePlantName.postValue(plant.mainCommonName)
            observablePlantScientificName.postValue(plant.exactName)
            observablePlantFamily.postValue(plant.family)
            observablePlantImageSource.postValue(plant.imageUrl)
            observablePlantCommonNames.postValue(plant.commonNames)
            observablePlantDescription.postValue(plantMetadata.description)
            observableSource.postValue(plantMetadata.source)
            when (plantMetadata.isToxic) {
                ToxicityValue.TOXIC -> {
                    observableDangerousText.postValue(when (animalType) {
                        AnimalType.CAT -> R.string.plant_details_cat_dangerous
                        AnimalType.DOG -> R.string.plant_details_dog_dangerous
                        AnimalType.ALL -> TODO()
                    })

                    observableDangerousColor.postValue(R.color.colorDanger)
                }
                ToxicityValue.NON_TOXIC -> {
                    observableDangerousText.postValue(when (animalType) {
                        AnimalType.CAT -> R.string.plant_details_cat_safe
                        AnimalType.DOG -> R.string.plant_details_dog_safe
                        AnimalType.ALL -> TODO()
                    })
                    observableDangerousColor.postValue(R.color.colorSafe)
                }
                ToxicityValue.UNDETERMINED -> {
                    observableDangerousText.postValue(when (animalType) {
                        AnimalType.CAT -> R.string.plant_details_cat_unknown
                        AnimalType.DOG -> R.string.plant_details_dog_unknown
                        AnimalType.ALL -> TODO()
                    })
                    observableDangerousColor.postValue(R.color.colorUndetermined)
                }
            }
        }
    }
}
