package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ModelStorageCommonTest {

    private lateinit var modelStorage: ModelStorageInterface
    private lateinit var modelStorageImpl: ModelStorage

    fun setUp(platformInitializer: ModelStoragePlatformInitializer) {
        val initializer = ModelStorageInitializer(platformInitializer)
        modelStorageImpl = ModelStorage(initializer)
        modelStorage = modelStorageImpl
    }

    fun tearDown() {

    }

    fun testGetPlants() {
        modelStorageImpl.test()
        val plants = modelStorage.getPlants(true)
        assertEquals(plants.size, 1)
    }

    fun testGetPlant() {
        modelStorageImpl.test()
        val plant = modelStorage.getPlant(1)
        assertNotNull(plant)
        assertEquals(plant.id, 1)
    }
}