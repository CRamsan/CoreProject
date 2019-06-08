package com.cramsan.petproject.storage

import com.cramsan.petproject.model.Plant

interface ModelStorageListenerInterface {
    fun onUpdate(plants: List<Plant>)
    fun onUpdate(plant: Plant)
}