package com.cramsan.petproject.mainmenu

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logI
import com.cramsan.framework.thread.assertIsUIThread
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.base.CatalogDownloadViewModel
import com.cramsan.petproject.base.LiveEvent
import com.cramsan.petproject.base.SimpleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.features.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllPlantListViewModel @Inject constructor(
    application: Application,
    modelProvider: ModelProviderInterface,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) :
    CatalogDownloadViewModel(application, dispatcherProvider, modelProvider, savedStateHandle) {

    override val logTag: String
        get() = "AllPlantListViewModel"

    // State
    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observablePlantListVisibility = MutableLiveData<Int>(View.VISIBLE)
    private val observableMenuVisibility = MutableLiveData<Int>(View.VISIBLE)
    private val observableLoadingVisibility = MutableLiveData<Int>(View.GONE)

    // Events
    private val observableNextActivityCat = LiveEvent<SimpleEvent>()
    private val observableNextActivityDog = LiveEvent<SimpleEvent>()

    fun observableLoadingVisibility(): LiveData<Int> = observableLoadingVisibility
    fun observablePlantListVisibility(): LiveData<Int> = observablePlantListVisibility
    fun observableMenuVisibility(): LiveData<Int> = observableMenuVisibility
    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableNextActivityCat(): LiveData<SimpleEvent> = observableNextActivityCat
    fun observableNextActivityDog(): LiveData<SimpleEvent> = observableNextActivityDog

    var queryString = MutableStateFlow("")

    init {
        observablePlants.value = emptyList()
        queryString.onEach {
            searchPlants(it)
        }.launchIn(viewModelScope)
    }

    fun goToCats(view: View) {
        goToNextActivity(AnimalType.CAT)
    }

    fun goToDogs(view: View) {
        goToNextActivity(AnimalType.DOG)
    }

    private fun goToNextActivity(animalType: AnimalType) {
        if (!isCatalogReady()) {
            observableShowIsDownloadingData.value = SimpleEvent()
            return
        }
        when (animalType) {
            AnimalType.CAT -> observableNextActivityCat.value = SimpleEvent()
            AnimalType.DOG -> observableNextActivityDog.value = SimpleEvent()
            AnimalType.ALL -> TODO()
        }
    }

    private fun setInSearchMode(isSearchMode: Boolean) {
        if (isSearchMode) {
            observableMenuVisibility.value = View.GONE
            observablePlantListVisibility.value = View.VISIBLE
        } else {
            observableMenuVisibility.value = View.VISIBLE
            observablePlantListVisibility.value = View.GONE
        }
    }

    override fun tryStartDownload() {
        // Restore the state of the plant list back to all.
        viewModelScope.launch(Dispatchers.IO) {
            modelProvider.getPlantsWithToxicity(AnimalType.ALL, "en")
        }
        viewModelScope.launch(Dispatchers.IO) {
            // Hit the plant API to warm up the endpoint
            try {
                modelProvider.getPlant(AnimalType.CAT, 0, "en")
            } catch (e: ServerResponseException) {
                // This failure is expected. We can safely ignore it.
            }
        }
        super.tryStartDownload()
    }

    private fun searchPlants(query: String) {
        logI("AllPlantListViewModel", "searchPlants")
        observableLoadingVisibility.value = View.VISIBLE

        if (query.isEmpty()) {
            setInSearchMode(false)
            observableLoadingVisibility.value = View.GONE
            return
        }

        setInSearchMode(true)
        viewModelScope.launch {
            filterPlants(query)
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicityFiltered(AnimalType.ALL, query, "en")
        viewModelScope.launch {
            assertIsUIThread()
            observableLoadingVisibility.value = View.GONE
            observablePlants.value = plants
        }
    }
}
