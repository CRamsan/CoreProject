package com.cramsan.petproject.plantdetails

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.LiveEvent
import com.cramsan.framework.core.StringEvent
import com.cramsan.framework.logging.logI
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.CatalogDownloadViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel that manages the plant details page.
 */
@HiltViewModel
class PlantDetailsViewModel @Inject constructor(
    application: Application,
    modelProvider: ModelProviderInterface,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : CatalogDownloadViewModel(application, dispatcherProvider, modelProvider, savedStateHandle) {

    override val logTag: String
        get() = "PlantDetailsViewModel"

    // State

    /**
     * Observable that represents the plant name.
     */
    val observablePlantName = MutableLiveData<String>()
    /**
     * Observable that represents the scientific name.
     */
    val observablePlantScientificName = MutableLiveData<String>()
    /**
     * Observable that represents the plant family.
     */
    val observablePlantFamily = MutableLiveData<String>()
    /**
     * Observable that represents the plant image as a URL.
     */
    val observablePlantImageSource = MutableLiveData<String>()
    /**
     * Observable that represents the plant name common names.
     */
    val observablePlantCommonNames = MutableLiveData<String>()
    /**
     * Observable that represents the plant description.
     */
    val observablePlantDescription = MutableLiveData<String>()
    /**
     * Observable that represents the URL to the source.
     */
    val observableSource = MutableLiveData<String>()
    /**
     * Observable that represents the text for danger.
     */
    val observableDangerousText = MutableLiveData<Int>(R.string.string_empty)
    /**
     * Observable that represents the color to represent the danger.
     */
    val observableDangerousColor = MutableLiveData<Int>(R.color.colorUndetermined)
    /**
     * Observable that represents the visibility of the common names.
     */
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

    /**
     * Observable that emits a URL. A fragment/activity is expected to observe this URL and handle it.
     */
    fun observableOpenSourceLink(): LiveData<StringEvent> = observableOpenSourceLink

    /**
     * Callback for when the user taps on the source link with the intent to open it.
     */
    fun openSourceLink(@Suppress("UNUSED_PARAMETER")view: View) {
        observableSource.value?.let {
            observableOpenSourceLink.value = StringEvent(it)
        }
    }

    /**
     * Load the information for the plant with id [plantId] and for animal [animalType].
     */
    fun reloadPlant(animalType: AnimalType, plantId: Int) {
        logI("PlantDetailsViewModel", "reloadPlant")
        viewModelScope.launch {
            loadPlant(animalType, plantId)
        }
    }

    @Suppress("ComplexMethod")
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
