package com.cramsan.petproject.plantslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantListViewModel(application: Application) : AndroidViewModel(application), KodeinAware {

    override val kodein by kodein(application)
    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()
    private val threadUtil: ThreadUtilInterface by instance()

    private val observablePlants = MutableLiveData<List<PresentablePlant>>()

    fun reloadPlants(animalType: AnimalType) {
        eventLogger.log(Severity.INFO, classTag(), "reloadPlants")
        viewModelScope.launch {
            loadPlants(animalType)
        }
    }

    fun searchPlants(query: String, animalType: AnimalType) {
        eventLogger.log(Severity.INFO, classTag(), "searchPlants")
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadPlants(animalType)
            } else {
                filterPlants(query, animalType)
            }
        }
    }

    fun observablePlants(): LiveData<List<PresentablePlant>> {
        return observablePlants
    }

    private suspend fun loadPlants(animalType: AnimalType) = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicity(animalType, "en")
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }

    private suspend fun filterPlants(query: String, animalType: AnimalType) = withContext(Dispatchers.IO) {
        val plants = modelProvider.getPlantsWithToxicityFiltered(animalType, query, "en") ?: return@withContext
        viewModelScope.launch {
            threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }
}
