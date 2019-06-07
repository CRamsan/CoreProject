package com.cramsan.petproject.storage.provider

import com.cramsan.petproject.model.Plant

interface ModelStorageInterface {
    fun registerListener(listener: ModelStorageListenerInterface)
    fun deregisterListener(listener: ModelStorageListenerInterface)
    fun getPlants(forceUpdate: Boolean): List<Plant>
}

interface ModelStorageListenerInterface {
    fun onUpdate(plants: List<Plant>)
}