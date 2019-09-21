package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.preferences.PlatformPreferencesInterface
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.preferences.implementation.PreferencesInitializer
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.Plant
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.newInstance

internal class ModelProviderCommonTest {

    private val kodein = Kodein {
        bind<EventLoggerInterface>() with provider { mockk<EventLogger>(relaxUnitFun = true) }
        bind<ThreadUtilInterface>() with provider { mockk<ThreadUtil>(relaxUnitFun = true) }
    }

    private lateinit var modelProvider: ModelProviderInterface
    private lateinit var modelProviderImpl: ModelProvider
    private lateinit var modelStorage: ModelStorage

    fun setUp(storagePlatformInitializer: ModelStoragePlatformInitializer, platformPreferencesInterface: PlatformPreferencesInterface) {
        val modelStorageImpl by kodein.newInstance { ModelStorage(ModelStorageInitializer(storagePlatformInitializer), instance(), instance()) }
        modelStorage = modelStorageImpl
        val preferences by kodein.newInstance { Preferences(PreferencesInitializer(platformPreferencesInterface)) }
        val newModelProvider by kodein.newInstance { ModelProvider(instance(), instance(), modelStorage, preferences) }
        modelProviderImpl = newModelProvider
        modelProvider = modelProviderImpl
    }

    suspend fun testFiltering() = coroutineScope {
        insertBaseEntries()
        modelProvider.getPlantsWithToxicity(AnimalType.CAT, "en")
        var result = modelProvider.getPlantsWithToxicityFiltered(AnimalType.CAT, "100", "en")
        assertNotNull(result)
        assertEquals(result.size, 1)
        for (i in 0..99) {
            launch {
                modelProvider.getPlantsWithToxicityFiltered(AnimalType.CAT, "100", "en")
            }
        }
        result = modelProvider.getPlantsWithToxicityFiltered(AnimalType.CAT, "65", "en")
        assertNotNull(result)
        assertEquals(result.size, 1)
    }

    private fun insertBaseEntries() {
        var descriptionId = 0L
        var familyId = 0L
        var commonNameId = 0L
        var mainNameId = 0L
        var toxicityId = 0L
        for (i in 1..100) {
            val plantId: Long = i.toLong()
            modelStorage.insertPlant(
                Plant.PlantImp(plantId,
                    "$i",
                    "https://www.aspca.org"))
            modelStorage.insertDescription(Description.DescriptionImpl(descriptionId++, plantId, AnimalType.CAT.ordinal, "en", "desc"))
            modelStorage.insertPlantFamily(PlantFamily.PlantFamilyImpl(familyId++, "Some Family $i", plantId, "en"))
            modelStorage.insertPlantCommonName(PlantCommonName.PlantCommonNameImpl(commonNameId++, "Name $i", plantId, "en"))
            modelStorage.insertPlantMainName(PlantMainName.PlantMainNameImpl(mainNameId++, "$i $i", plantId, "en"))
            modelStorage.insertToxicity(Toxicity.ToxicityImpl(toxicityId++, plantId, AnimalType.CAT.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
            modelStorage.insertToxicity(Toxicity.ToxicityImpl(toxicityId++, plantId, AnimalType.DOG.ordinal, ToxicityValue.NON_TOXIC.ordinal, ""))
        }
    }
}
