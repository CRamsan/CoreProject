package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.Plant
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
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

    fun endTest() {
        modelStorage.deleteAll()
    }

    fun getPlants() {
        insertBaseEntries()
        var allPlants = modelStorage.getPlants()
        assertEquals(100, allPlants.size)

        val plantId: Long = 25432
        modelStorage.insertPlant(Plant.PlantImp(plantId,
            "Arum maculatum TEST",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"))
        modelStorage.insertDescription(Description.DescriptionImpl(2349, plantId, AnimalType.CAT.ordinal, "en", "desc"))
        modelStorage.insertPlantFamily(PlantFamily.PlantFamilyImpl(342, "Some Family", plantId, "en"))
        modelStorage.insertPlantCommonName(PlantCommonName.PlantCommonNameImpl(435, "Name 5", plantId, "en"))
        modelStorage.insertPlantMainName(PlantMainName.PlantMainNameImpl(234, "Adam and Eve test", plantId, "en"))
        modelStorage.insertToxicity(Toxicity.ToxicityImpl(432, plantId, AnimalType.CAT.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
        modelStorage.insertToxicity(Toxicity.ToxicityImpl(432, plantId, AnimalType.DOG.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))

        allPlants = modelStorage.getPlants()
        assertEquals(101, allPlants.size)
    }

    fun getPlant() {
        val plantId: Long = 25432
        modelStorage.insertPlant(Plant.PlantImp(plantId,
            "Arum maculatum TEST",
            "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"))
        modelStorage.insertDescription(Description.DescriptionImpl(2349, plantId, AnimalType.CAT.ordinal, "en", "desc"))
        modelStorage.insertPlantFamily(PlantFamily.PlantFamilyImpl(342, "Some Family", plantId, "en"))
        modelStorage.insertPlantCommonName(PlantCommonName.PlantCommonNameImpl(435, "Name 5", plantId, "en"))
        modelStorage.insertPlantMainName(PlantMainName.PlantMainNameImpl(234, "Adam and Eve test", plantId, "en"))
        modelStorage.insertToxicity(Toxicity.ToxicityImpl(432, plantId, AnimalType.CAT.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
        modelStorage.insertToxicity(Toxicity.ToxicityImpl(745, plantId, AnimalType.DOG.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
        val allPlants = modelStorage.getPlants()
        assertEquals(1, allPlants.size)

        val plant = modelStorage.getCustomPlantEntry(AnimalType.CAT, plantId.toInt(), "en")
        assertNotNull(plant)
    }

    fun getPlantMetadata() {
        insertBaseEntries()

        val plant = modelStorage.getPlants()[10]
        assertNotNull(modelStorage.getCustomPlantEntry(AnimalType.CAT, plant.id.toInt(), "en"))
    }

    fun deleteAll() {
        insertBaseEntries()
        val allPlants = modelStorage.getPlants()
        assertEquals(100, allPlants.size)
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
        for (i in 1..100) {
            val plantId: Long = i.toLong()
            modelStorage.insertPlant(Plant.PlantImp(plantId,
                "Arum maculatum TEST $i",
                "https://www.aspca.org/sites/default/files/styles/medium_image_300x200/public/field/image/plants/arum-r.jpg?itok=206UUxCJ"))
            modelStorage.insertDescription(Description.DescriptionImpl(descriptionId++, plantId, AnimalType.CAT.ordinal, "en", "desc"))
            modelStorage.insertPlantFamily(PlantFamily.PlantFamilyImpl(familyId++, "Some Family $i", plantId, "en"))
            modelStorage.insertPlantCommonName(PlantCommonName.PlantCommonNameImpl(commonNameId++, "Name $i", plantId, "en"))
            modelStorage.insertPlantMainName(PlantMainName.PlantMainNameImpl(mainNameId++, "Adam and Eve $i", plantId, "en"))
            modelStorage.insertToxicity(Toxicity.ToxicityImpl(toxicityId++, plantId, AnimalType.CAT.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
            modelStorage.insertToxicity(Toxicity.ToxicityImpl(toxicityId++, plantId, AnimalType.DOG.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
        }
    }
}
