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

open class AllPlantListViewModel(application: Application) : AndroidViewModel(application), KodeinAware,
    ModelProviderEventListenerInterface {

    override val kodein by kodein(application)
    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()
    private val threadUtil: ThreadUtilInterface by instance()

    private val observableInSearchMode = MutableLiveData<Boolean>()
    private val observablePlants = MutableLiveData<List<PresentablePlant>>()
    private val observableLoading = MutableLiveData<Boolean>()
    private val observableDownloading = MutableLiveData<Boolean>()

    fun observablePlants(): LiveData<List<PresentablePlant>> = observablePlants
    fun observableLoading(): LiveData<Boolean> = observableLoading
    fun observableInSearchMode(): LiveData<Boolean> = observableInSearchMode
    fun observableDownloading(): LiveData<Boolean> = observableDownloading

    init {
        modelProvider.registerForCatalogEvents(this)
    }

    override fun onCleared() {
        modelProvider.deregisterForCatalogEvents(this)
    }

    override fun onCatalogUpdate(isReady: Boolean) {
        viewModelScope.launch {
            observableLoading.value = !isReady
            if (isReady) {
                reloadPlants()
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

        if (query.isEmpty()) {
            observableInSearchMode.value = false
            return
        }

        observableInSearchMode.value = true
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadPlants()
            } else {
                filterPlants(query)
            }
        }
    }

    private suspend fun loadPlants() = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicity(AnimalType.ALL, "en")
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observableLoading.value = false
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

    fun downloadCatalog() {
        eventLogger.log(Severity.INFO, "AllPlantListViewModel", "reloadPlants")
        val unixTime = System.currentTimeMillis() / 1000L
        if (modelProvider.isCatalogAvailable(unixTime)) {
            observableDownloading.value = false
            return
        }
        observableDownloading.value = true
        viewModelScope.launch {
            downloadCatalogOnBackground()
        }
    }

    private suspend fun downloadCatalogOnBackground() = withContext(Dispatchers.IO) {
        val unixTime = System.currentTimeMillis() / 1000L
        modelProvider.downloadCatalog(unixTime)
        viewModelScope.launch {
            observableDownloading.value = false
        }
    }
}
