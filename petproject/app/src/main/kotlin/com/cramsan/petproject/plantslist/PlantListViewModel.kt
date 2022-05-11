package com.cramsan.petproject.plantslist

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logW
import com.cramsan.framework.thread.assertIsUIThread
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.CatalogDownloadViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel that manages the plant-list screen.
 */
@HiltViewModel
class PlantListViewModel @Inject constructor(
    application: Application,
    modelProvider: ModelProviderInterface,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) :
    CatalogDownloadViewModel(application, dispatcherProvider, modelProvider, savedStateHandle) {

    override val logTag: String
        get() = "PlantListViewModel"

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableAnimalType = MutableLiveData<AnimalType>()
    private val observableLoadingVisibility = MutableLiveData<Int>(View.GONE)
    private val observablePlantListVisibility = MutableLiveData<Int>(View.GONE)

    /**
     * Observable value for the visibility of the loading view.
     */
    fun observableLoadingVisibility(): LiveData<Int> = observableLoadingVisibility
    /**
     * Observable value for the visibility of the list of plants.
     */
    fun observablePlantListVisibility(): LiveData<Int> = observablePlantListVisibility
    /**
     * Observable that holds the list of plants to display.
     */
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    /**
     * Allow for the fragment to set the value of the search query.
     */
    var queryString = MutableStateFlow("")

    init {
        queryString.onEach {
            searchPlants(it)
        }.launchIn(viewModelScope)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            setLoadingMode(!isReady)
        }
    }

    /**
     * Set the [animalType] that will be used for this screen.
     */
    fun setAnimalType(animalType: AnimalType) {
        observableAnimalType.value = animalType
        viewModelScope.launch {
            filterPlants(queryString.value)
        }
    }

    /**
     * Set the loading mode of this ViewModel.
     */
    fun setLoadingMode(isSearchMode: Boolean) {
        if (isSearchMode) {
            observableLoadingVisibility.value = View.VISIBLE
            observablePlantListVisibility.postValue(View.GONE)
        } else {
            observableLoadingVisibility.value = View.GONE
            observablePlantListVisibility.postValue(View.VISIBLE)
        }
    }

    private fun searchPlants(query: String) {
        logI("PlantListViewModel", "searchPlants")
        viewModelScope.launch {
            filterPlants(query)
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val animalType = observableAnimalType.value
        viewModelScope.launch {
            setLoadingMode(true)
        }

        if (animalType == null) {
            logW(
                "PlantListViewModel",
                "Unable to filterPlants. AnimalType is null",
            )
            viewModelScope.launch {
                setLoadingMode(true)
            }
            return@withContext
        }

        val plants: List<PresentablePlant> = if (query.isEmpty()) {
            modelProvider.getPlantsWithToxicity(animalType, "en")
        } else {
            modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en")
        }

        viewModelScope.launch {
            assertIsUIThread()
            setLoadingMode(false)
            observablePlants.value = plants
        }
    }
}
