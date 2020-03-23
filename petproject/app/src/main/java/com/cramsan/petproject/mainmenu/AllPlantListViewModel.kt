package com.cramsan.petproject.mainmenu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderEventListenerInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class AllPlantListViewModel(application: Application) : AndroidViewModel(application), KodeinAware,
    ModelProviderEventListenerInterface {

    override val kodein by kodein(application)
    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()
    private val threadUtil: ThreadUtilInterface by instance()

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableLoading = MutableLiveData<Boolean>()
    var animalType: AnimalType = AnimalType.CAT

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    override fun onCleared() {
        modelProvider.deregisterForCatalogEvents(this)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            if (isReady) {
                reloadPlants()
            } else {
                observableLoading.value = true
            }
        }
    }

    fun reloadPlants() {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "reloadPlants")
        observableLoading.value = true
        viewModelScope.launch {
            loadPlants()
        }
    }

    fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "searchPlants")
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadPlants()
            } else {
                filterPlants(query)
            }
        }
    }

    fun observablePlants(): LiveData<List<PresentablePlant>> {
        return observablePlants
    }

    fun observableLoading(): LiveData<Boolean> {
        return observableLoading
    }

    private suspend fun loadPlants() = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicity(animalType, "en")
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observableLoading.value = false
            observablePlants.value = plants
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en") ?: return@withContext
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }
}
