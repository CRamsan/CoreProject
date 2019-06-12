package com.cramsan.petproject.storage.sqlite

import com.cramsan.petproject.model.Plant

interface DAOInterface {
    fun getPlants(forceUpdate: Boolean): List<Plant>

    fun getPlant(uniqueName: String): Plant?

    fun getItems(forceUpdate: Boolean)
}