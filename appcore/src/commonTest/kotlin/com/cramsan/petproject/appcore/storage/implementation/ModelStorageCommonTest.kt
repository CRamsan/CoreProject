package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.Plant
import com.cramsan.petproject.appcore.model.PlantMetadata
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.newInstance

@Suppress("MagicNumber")
internal class ModelStorageCommonTest {

    private val kodein = Kodein {
        bind<EventLoggerInterface>() with provider { mockk<EventLogger>(relaxUnitFun = true) }
        bind<ThreadUtilInterface>() with provider { mockk<ThreadUtil>(relaxUnitFun = true) }
    }

    private lateinit var modelStorage: ModelStorageInterface

    fun setUp(platformInitializer: ModelStoragePlatformInitializer) {
        val initializer = ModelStorageInitializer(platformInitializer)
        val newModelStorage by kodein.newInstance { ModelStorage(initializer, instance(), instance()) }
        modelStorage = newModelStorage
    }

    fun getPlants() {
        insertBaseEntries()
        var allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(100, allPlants.size)

        modelStorage.insertPlant(Plant(0,
            "Arum maculatum TEST",
            "Adam and Eve test",
            "Araceae, Some Other name",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ",
            "Family something"
        ), PlantMetadata(
            0, AnimalType.CAT, true, "", ""), "en")
        allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(101, allPlants.size)
    }

    fun getPlant() {
        modelStorage.insertPlant(Plant(0,
            "Arum maculatum TEST",
            "Adam and Eve test",
            "Araceae, Some Other name",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ",
            "Family something"
        ), PlantMetadata(
            0, AnimalType.CAT, true, "", ""), "en")
        val allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(1, allPlants.size)

        val plant = modelStorage.getPlant(AnimalType.CAT, allPlants.first().id, "en")
        assertNotNull(plant)
    }

    fun getPlantMetadata() {
        insertBaseEntries()

        val plant = modelStorage.getPlants(AnimalType.CAT, "en")[10]
        assertNotNull(modelStorage.getPlantMetadata(AnimalType.CAT, plant.id, "en"))
    }

    fun deleteAll() {
        insertBaseEntries()
        val allPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(100, allPlants.size)
        modelStorage.deleteAll()
        val newAllPlants = modelStorage.getPlants(AnimalType.CAT, "en")
        assertEquals(0, newAllPlants.size)
    }

    fun insertPlant() {
        modelStorage.insertPlant(Plant(1, "Howea forsteriana", "Forster Sentry Palm", "Kentia palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Palmea"), PlantMetadata(1, AnimalType.DOG, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/forster-sentry-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana", "Paradise Palm", "Kentia palm, Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.CAT, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/paradise-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana", "Paradise Palm", "Kentia palm, Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.DOG, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/paradise-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana", "Forster Sentry Palm", "Kentia palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Palmea"), PlantMetadata(1, AnimalType.CAT, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/forster-sentry-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana", "Kentia Palm", "Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.CAT, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/kentia-palm"), "en")
        modelStorage.insertPlant(Plant(1, "Howea forsteriana", "Kentia Palm", "Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana"), PlantMetadata(1, AnimalType.DOG, false, "", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/kentia-palm"), "en")

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
        for (i in 1..100) {
            modelStorage.insertPlant(Plant(1, "Howea forsteriana $i", "Kentia Palm $i", "Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana $i"), PlantMetadata(1, AnimalType.DOG, true, "$i", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/kentia-palm"), "en")
            modelStorage.insertPlant(Plant(1, "Howea forsteriana $i", "Kentia Palm $i", "Forster Senty Palm", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg", "Howea forsteriana $i"), PlantMetadata(1, AnimalType.CAT, false, "$i", "https://www.aspca.org/pet-care/animal-poison-control/toxic-and-non-toxic-plants/kentia-palm"), "en")
        }
    }
}
