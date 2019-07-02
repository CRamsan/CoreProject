package com.cramsan.petproject.plantdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantDetailsViewModel : ViewModel() {

    private val modelStore = CoreFrameworkAPI.modelStorage

    private val observablePlant = MutableLiveData<Plant>()

    fun reloadPlant(plantId: Int) {
        viewModelScope.launch {
            loadPlant(plantId)
        }
    }

    fun getPlant(): LiveData<Plant> {
        return observablePlant
    }

    private suspend fun loadPlant(plantId: Int) = withContext(Dispatchers.IO) {
        val plant = modelStore.getPlant(plantId)
        viewModelScope.launch {
            observablePlant.value = plant
        }
    }
}
