package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ModelStorageCommonTest {

    private lateinit var modelStorage: ModelStorageInterface

    fun setUp(platformInitializer: ModelStoragePlatformInitializer) {
        val initializer = ModelStorageInitializer(platformInitializer)
        modelStorage = ModelStorage(initializer)
    }

    fun tearDown() {

    }

    suspend fun testGetPlants() {
        val plants = modelStorage.getPlants(true)
        assertEquals(plants.size, 1)
    }

    suspend fun testGetPlant() {
        val plant = modelStorage.getPlant(1)
        assertNotNull(plant)
        assertEquals(plant.id, 1)
    }
}