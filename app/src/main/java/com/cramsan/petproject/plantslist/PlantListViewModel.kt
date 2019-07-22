package com.cramsan.petproject.plantslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantListViewModel : ViewModel() {

    private val modelStore = CoreFrameworkAPI.modelProvider

    private val observablePlants = MutableLiveData<List<Plant>>()

    fun reloadPlants() {
        viewModelScope.launch {
            loadPlants()
        }
    }

    fun getPlants(): LiveData<List<Plant>> {
        return observablePlants
    }

    private suspend fun loadPlants() = withContext(Dispatchers.IO)  {
        val plants = modelStore.getPlants(AnimalType.CAT, "en")
        viewModelScope.launch {
            CoreFrameworkAPI.threadUtil.assertIsUIThread()
            observablePlants.value = plants
        }
    }
}
