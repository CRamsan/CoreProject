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
import kotlinx.android.synthetic.main.fragment_main_menu.*
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

    private val observableInSearchMode = MutableLiveData<Boolean>()
    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableIsCatalogReady = MutableLiveData<Boolean>()

    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableInSearchMode(): LiveData<Boolean> = observableInSearchMode
    fun observableIsCatalogReady(): LiveData<Boolean> = observableIsCatalogReady

    init {
        modelProvider.registerForCatalogEvents(this)
        observablePlants.value = emptyList()
    }

    override fun onCleared() {
        modelProvider.deregisterForCatalogEvents(this)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            observableIsCatalogReady.value = isReady
            if (isReady) {
                loadPlants()
            }
        }
    }

    fun searchPlants(query: String) {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "searchPlants")

        if (query.isEmpty()) {
            observableInSearchMode.value = false
            viewModelScope.launch {
                loadPlants()
            }
            return
        }

        observableInSearchMode.value = true
        viewModelScope.launch {
            filterPlants(query)
        }
    }

    private suspend fun loadPlants() = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicity(AnimalType.ALL, "en")
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }

    private suspend fun filterPlants(query: String) = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicityFiltered(AnimalType.ALL, query, "en") ?: return@withContext
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }

    fun isCatalogReady(): Boolean {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "isCatalogReady")
        val unixTime = System.currentTimeMillis() / 1000L
        return modelProvider.isCatalogAvailable(unixTime)
    }
}
