package com.cramsan.petproject.plantslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cramsan.petproject.framework.CoreFramework
import com.cramsan.petproject.model.Plant
import com.cramsan.petproject.storage.ModelStorageListenerInterface

class PlantListViewModel : ViewModel(), ModelStorageListenerInterface {

    private val modelStore = CoreFramework.modelStorage.also {
        it.registerListener(this)
    }
    private val observablePlants = MutableLiveData<List<Plant>>()

    init {
        modelStore.getPlants(true)
    }

    override fun onCleared() {
        modelStore.deregisterListener(this)
    }

    fun getPlants(): LiveData<List<Plant>> {
        return observablePlants
    }

    override fun onUpdate(plants: List<Plant>) {
        observablePlants.value = plants
    }
}