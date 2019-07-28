package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import kotlin.test.assertEquals

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

    fun getPlantMetadata() {
    }

    fun deleteAll() {
        insertBaseEntries()
        val allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(101, allPlants.size)
        modelStorageImpl.deleteAll()
        val newAllPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(0, newAllPlants.size)
    }

    private fun insertBaseEntries() {
        modelStorageImpl.sqlDelightDAO.insertPlantEntry("Arum maculatum",
            "Adam and Eve",
            "Araceae",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
        )
        val newPlant = modelStorageImpl.sqlDelightDAO.getPlantEntry("Arum maculatum")
        modelStorageImpl.sqlDelightDAO.insertPlantMainNameEntry("Arum", newPlant.id, "en")
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
            modelStorageImpl.sqlDelightDAO.insertPlantMainNameEntry("Arum", new.id, "en")
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
