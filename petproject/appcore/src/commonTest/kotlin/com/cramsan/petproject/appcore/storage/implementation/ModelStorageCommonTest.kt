package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.ModelStoragePlatformProvider
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Suppress("MagicNumber")
internal class ModelStorageCommonTest {

    private lateinit var log: EventLoggerInterface
    private lateinit var threadUtil: ThreadUtilInterface

    private lateinit var modelStorage: ModelStorageInterface

    fun setUp(platformProvider: ModelStoragePlatformProvider) {
        log = mockk<EventLogger>(relaxUnitFun = true)
        threadUtil = mockk<ThreadUtil>(relaxUnitFun = true)
        val newModelStorage = ModelStorage(platformProvider.provide(), log, threadUtil)
        modelStorage = newModelStorage
    }

    fun endTest() {
        modelStorage.deleteAll()
    }

    fun getPlants() {
        insertBaseEntries()
        var allPlants = modelStorage.getPlants()
        assertEquals(10, allPlants.size)

        val plantId: Long = 25432
        modelStorage.insertPlant(
            PlantImp(
                plantId,
                "Arum maculatum TEST",
                "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
            )
        )
        modelStorage.insertDescription(DescriptionImpl(2349, plantId, AnimalType.CAT, "en", "desc"))
        modelStorage.insertPlantFamily(PlantFamilyImpl(342, "Some Family", plantId, "en"))
        modelStorage.insertPlantCommonName(PlantCommonNameImpl(435, "Name 5", plantId, "en"))
        modelStorage.insertPlantMainName(PlantMainNameImpl(234, "Adam and Eve test", plantId, "en"))
        modelStorage.insertToxicity(ToxicityImpl(432, plantId, AnimalType.CAT, ToxicityValue.NON_TOXIC, ""))
        modelStorage.insertToxicity(ToxicityImpl(432, plantId, AnimalType.DOG, ToxicityValue.NON_TOXIC, ""))

        allPlants = modelStorage.getPlants()
        assertEquals(11, allPlants.size)
    }

    fun getPlant() {
        val plantId: Long = 25432
        modelStorage.insertPlant(
            PlantImp(
                plantId,
                "Arum maculatum TEST",
                "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
            )
        )
        modelStorage.insertDescription(DescriptionImpl(2349, plantId, AnimalType.CAT, "en", "desc"))
        modelStorage.insertPlantFamily(PlantFamilyImpl(342, "Some Family", plantId, "en"))
        modelStorage.insertPlantCommonName(PlantCommonNameImpl(435, "Name 5", plantId, "en"))
        modelStorage.insertPlantMainName(PlantMainNameImpl(234, "Adam and Eve test", plantId, "en"))
        modelStorage.insertToxicity(ToxicityImpl(432, plantId, AnimalType.CAT, ToxicityValue.NON_TOXIC, ""))
        modelStorage.insertToxicity(ToxicityImpl(745, plantId, AnimalType.DOG, ToxicityValue.NON_TOXIC, ""))
        val allPlants = modelStorage.getPlants()
        assertEquals(1, allPlants.size)

        val plant = modelStorage.getCustomPlantEntry(AnimalType.CAT, plantId.toInt(), "en")
        assertNotNull(plant)
    }

    fun getPlantMetadata() {
        insertBaseEntries()

        val plant = modelStorage.getPlants()[9]
        assertNotNull(modelStorage.getCustomPlantEntry(AnimalType.CAT, plant.id.toInt(), "en"))
    }

    fun deleteAll() {
        insertBaseEntries()
        val allPlants = modelStorage.getPlants()
        assertEquals(10, allPlants.size)
        modelStorage.deleteAll()
        val newAllPlants = modelStorage.getPlants()
        assertEquals(0, newAllPlants.size)
    }

    private fun insertBaseEntries() {

        var descriptionId = 0L
        var familyId = 0L
        var commonNameId = 0L
        var mainNameId = 0L
        var toxicityId = 0L
        for (i in 1..10) {
            val plantId: Long = i.toLong()
            modelStorage.insertPlant(
                PlantImp(
                    plantId,
                    "Arum maculatum TEST $i",
                    "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"
                )
            )
            modelStorage.insertDescription(DescriptionImpl(descriptionId++, plantId, AnimalType.CAT, "en", "desc"))
            modelStorage.insertPlantFamily(PlantFamilyImpl(familyId++, "Some Family $i", plantId, "en"))
            modelStorage.insertPlantCommonName(PlantCommonNameImpl(commonNameId++, "Name $i", plantId, "en"))
            modelStorage.insertPlantMainName(PlantMainNameImpl(mainNameId++, "Adam and Eve $i", plantId, "en"))
            modelStorage.insertToxicity(ToxicityImpl(toxicityId++, plantId, AnimalType.CAT, ToxicityValue.NON_TOXIC, ""))
            modelStorage.insertToxicity(ToxicityImpl(toxicityId++, plantId, AnimalType.DOG, ToxicityValue.NON_TOXIC, ""))
        }
    }
}
