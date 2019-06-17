package com.cramsan.petproject.plantdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cramsan.petproject.appcore.framework.CoreFramework
import com.cramsan.petproject.appcore.model.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantDetailsViewModel : ViewModel() {

    private val modelStore = CoreFramework.modelStorage

    private val observablePlant = MutableLiveData<Plant>()

    fun reloadPlant(uniqueName: String) {
        viewModelScope.launch {
            loadPlant(uniqueName)
        }
    }

    fun getPlant(): LiveData<Plant> {
        return observablePlant
    }

    private suspend fun loadPlant(uniqueName: String) = withContext(Dispatchers.IO) {
        val plant = modelStore.getPlant(uniqueName)
        viewModelScope.launch {
            observablePlant.value = plant
        }
    }
}
