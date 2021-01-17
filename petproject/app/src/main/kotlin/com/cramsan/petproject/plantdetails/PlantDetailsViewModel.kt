package com.cramsan.petproject.plantdetails

import android.app.Application
import android.view.View
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logI
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.CatalogDownloadViewModel
import com.cramsan.petproject.base.LiveEvent
import com.cramsan.petproject.base.StringEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantDetailsViewModel @ViewModelInject constructor(
    application: Application,
    modelProvider: ModelProviderInterface,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle
) : CatalogDownloadViewModel(application, dispatcherProvider, modelProvider, savedStateHandle) {

    override val logTag: String
        get() = "PlantDetailsViewModel"

    // State
    val observablePlantName = MutableLiveData<String>()
    val observablePlantScientificName = MutableLiveData<String>()
    val observablePlantFamily = MutableLiveData<String>()
    val observablePlantImageSource = MutableLiveData<String>()
    val observablePlantCommonNames = MutableLiveData<String>()
    val observablePlantDescription = MutableLiveData<String>()
    val observableSource = MutableLiveData<String>()
    val observableDangerousText = MutableLiveData<Int>(R.string.string_empty)
    val observableDangerousColor = MutableLiveData<Int>(R.color.colorUndetermined)
    val observablePlantCommonNamesVisibility =
        Transformations.map(observablePlantCommonNames) { commonName ->
            if (commonName.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

    // Events
    private val observableOpenSourceLink = LiveEvent<StringEvent>()

    fun observableOpenSourceLink() = observableOpenSourceLink

    fun openSourceLink(view: View) {
        observableSource.value?.let {
            observableOpenSourceLink.value = StringEvent(it)
        }
    }

    fun reloadPlant(animalType: AnimalType, plantId: Int) {
        logI("PlantDetailsViewModel", "reloadPlant")
        viewModelScope.launch {
            loadPlant(animalType, plantId)
        }
    }

    private suspend fun loadPlant(animalType: AnimalType, plantId: Int) =
        withContext(Dispatchers.IO) {
            val plant = modelProvider.getPlant(animalType, plantId, "en")
            val plantMetadata = modelProvider.getPlantMetadata(animalType, plantId, "en")
            if (plant == null || plantMetadata == null) {
                // TODO: Show error message
                TODO()
            }
            viewModelScope.launch {
                observablePlantName.value = plant.mainCommonName
                observablePlantScientificName.value = plant.exactName
                observablePlantFamily.value = plant.family
                observablePlantImageSource.value = plant.imageUrl
                observablePlantCommonNames.value = plant.commonNames
                observablePlantDescription.value = plantMetadata.description
                observableSource.value = plantMetadata.source

                when (plantMetadata.isToxic) {
                    ToxicityValue.TOXIC -> {
                        observableDangerousText.value = when (animalType) {
                            AnimalType.CAT -> R.string.plant_details_cat_dangerous
                            AnimalType.DOG -> R.string.plant_details_dog_dangerous
                            AnimalType.ALL -> TODO()
                        }

                        observableDangerousColor.value = R.color.colorDanger
                    }
                    ToxicityValue.NON_TOXIC -> {
                        observableDangerousText.value = when (animalType) {
                            AnimalType.CAT -> R.string.plant_details_cat_safe
                            AnimalType.DOG -> R.string.plant_details_dog_safe
                            AnimalType.ALL -> TODO()
                        }
                        observableDangerousColor.value = R.color.colorSafe
                    }
                    ToxicityValue.UNDETERMINED -> {
                        observableDangerousText.value = when (animalType) {
                            AnimalType.CAT -> R.string.plant_details_cat_unknown
                            AnimalType.DOG -> R.string.plant_details_dog_unknown
                            AnimalType.ALL -> TODO()
                        }
                        observableDangerousColor.value = R.color.colorUndetermined
                    }
                }
            }
        }
}
