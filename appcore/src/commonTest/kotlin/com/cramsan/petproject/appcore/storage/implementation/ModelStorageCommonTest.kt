package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
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

    fun getPlants() {
        insertBaseEntries()
        var allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(101, allPlants.size)

        modelStorageImpl.insertPlant(Plant(0,
            "Arum maculatum TEST",
            "Adam and Eve test",
            "Araceae, Some Other name",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ",
            "Family something"
        ), PlantMetadata(
            0,AnimalType.CAT , true, "", ""), "en")
        allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(102, allPlants.size)
    }

    fun getPlant() {
        modelStorageImpl.insertPlant(Plant(0,
            "Arum maculatum TEST",
            "Adam and Eve test",
            "Araceae, Some Other name",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ",
            "Family something"
        ), PlantMetadata(
            0,AnimalType.CAT , true, "", ""), "en")
        val allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(1, allPlants.size)

        val plant = modelStorage.getPlant(AnimalType.CAT, allPlants.first().id, "en")
        assertNotNull(plant)
    }

    fun getPlantMetadata() {
        insertBaseEntries()
        assertNotNull(modelStorage.getPlantMetadata(AnimalType.CAT, 10, "en"))
    }

    fun deleteAll() {
        insertBaseEntries()
        val allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(101, allPlants.size)
        modelStorageImpl.deleteAll()
        val newAllPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(0, newAllPlants.size)
    }

    fun insertPlant() {
        modelStorage.insertPlant(Plant(1, "Howea forsteriana" , "Forster Sentry Palm", "Kentia palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Palmea"), PlantMetadata(1, AnimalType.DOG, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/forster-sentry-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana" , "Paradise Palm", "Kentia palm, Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.CAT, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/paradise-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana" , "Paradise Palm", "Kentia palm, Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.DOG, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/paradise-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana" , "Forster Sentry Palm", "Kentia palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Palmea"), PlantMetadata(1, AnimalType.CAT, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/forster-sentry-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana" , "Kentia Palm", "Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.CAT, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/kentia-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana" , "Kentia Palm", "Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.DOG, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/kentia-palm"), "en")

        val allPlantsForCats = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(1, allPlantsForCats.size)

        val allPlantsForDogs = modelStorage.getPlants(AnimalType.DOG, "en")
        assertEquals(1, allPlantsForDogs.size)

        val plant = allPlantsForDogs.first()
        assertEquals("Forster Sentry Palm", plant.mainCommonName)
        assertEquals("Palmea", plant.family)
        val commonNames = modelStorage.getPlant(AnimalType.CAT, plant.id, "en")?.commonNames?.split(", ")
        assertEquals(2, commonNames?.size)
    }

    private fun insertBaseEntries() {
        modelStorageImpl.sqlDelightDAO.insertPlantEntry("Arum maculatum",
            "Adam and Eve",
            "Araceae",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
        )
        val newPlant = modelStorageImpl.sqlDelightDAO.getPlantEntry("Arum maculatum")
        modelStorageImpl.sqlDelightDAO.insertPlantMainNameEntry("Arum", newPlant!!.id, "en")
        modelStorageImpl.sqlDelightDAO.insertPlantFamilyNameEntry("Arum", newPlant.id, "en")
        modelStorageImpl.sqlDelightDAO.insertPlantCommonNameEntry("Arum", newPlant.id, "en")
        modelStorageImpl.sqlDelightDAO.insertPlantCommonNameEntry("Lord-and-Ladies", newPlant.id, "en")
        modelStorageImpl.sqlDelightDAO.insertPlantCommonNameEntry("Wake Robin", newPlant.id, "en")
        modelStorageImpl.sqlDelightDAO.insertToxicityEntry(true,
            newPlant.id,
            AnimalType.DOG,
            "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
        )
        modelStorageImpl.sqlDelightDAO.insertToxicityEntry(false,
            newPlant.id,
            AnimalType.CAT,
            "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
        )

        for (i in 1..100) {
            modelStorageImpl.sqlDelightDAO.insertPlantEntry(
                "Arum maculatum $i",
                "Adam and Eve $i",
                "Araceae",
                "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
            )
            val new = modelStorageImpl.sqlDelightDAO.getPlantEntry("Arum maculatum $i")
            modelStorageImpl.sqlDelightDAO.insertPlantMainNameEntry("Arum", new!!.id, "en")
            modelStorageImpl.sqlDelightDAO.insertPlantFamilyNameEntry("Arum", new.id, "en")
            modelStorageImpl.sqlDelightDAO.insertPlantCommonNameEntry("Arum", new.id, "en")
            modelStorageImpl.sqlDelightDAO.insertToxicityEntry(true,
                new.id,
                AnimalType.CAT,
                "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/adam-and-eve"
            )
        }
    }

}
