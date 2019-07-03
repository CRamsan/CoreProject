package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.Plant

interface ModelStorageInterface {
    fun getPlants(forceUpdate: Boolean): List<Plant>

    fun getPlant(plantId: Int): Plant?
}