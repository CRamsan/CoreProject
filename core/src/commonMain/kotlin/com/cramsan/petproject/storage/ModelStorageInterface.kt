package com.cramsan.petproject.storage

import com.cramsan.petproject.model.Plant

interface ModelStorageInterface {
    fun getPlants(forceUpdate: Boolean): List<Plant>

    fun getPlant(uniqueName: String): Plant?

    fun getItems(forceUpdate: Boolean)
}