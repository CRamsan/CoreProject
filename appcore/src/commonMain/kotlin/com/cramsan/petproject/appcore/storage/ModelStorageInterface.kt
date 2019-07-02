package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.model.Plant

interface ModelStorageInterface {
    suspend fun getPlants(forceUpdate: Boolean): List<Plant>

    suspend fun getPlant(plantId: Int): Plant?
}