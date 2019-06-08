package com.cramsan.petproject.plantdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cramsan.petproject.framework.CoreFramework
import com.cramsan.petproject.model.Plant
import com.cramsan.petproject.storage.ModelStorageListenerInterface

class PlantDetailsViewModel : ViewModel(), ModelStorageListenerInterface {

    private val modelStore = CoreFramework.modelStorage.also {
        it.registerListener(this)
    }

    private val observablePlant = MutableLiveData<Plant>()

    init {
        modelStore.getPlants(false)
    }

    override fun onCleared() {
        modelStore.deregisterListener(this)
    }

    fun getPlant(): LiveData<Plant> {
        return observablePlant
    }

    fun loadPlant(uniqueName: String) {
        modelStore.getPlant(uniqueName)
    }

    override fun onUpdate(plants: List<Plant>) { }

    override fun onUpdate(plant: Plant) {
        observablePlant.value = plant
    }
}
