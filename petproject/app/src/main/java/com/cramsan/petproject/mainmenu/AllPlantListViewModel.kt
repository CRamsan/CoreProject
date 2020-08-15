package com.cramsan.petproject.mainmenu

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.base.CatalogDownloadViewModel
import com.cramsan.petproject.base.LiveEvent
import com.cramsan.petproject.base.SimpleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class AllPlantListViewModel(application: Application) :
    CatalogDownloadViewModel(application) {

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

    var queryString: String by Delegates.observable("") {
        _, _, new ->
        searchPlants(new)
    }

    init {
        observablePlants.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
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
        observableNextActivityCat.value = SimpleEvent()
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
        super.tryStartDownload()
    }

    private fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "searchPlants")
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
        val plants = modelProvider.getPlantsWithToxicityFiltered(AnimalType.ALL, query, "en") ?: return@withContext
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observableLoadingVisibility.value = View.GONE
            observablePlants.value = plants
        }
    }
}
