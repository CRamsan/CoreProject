package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.ProviderConfig
import com.cramsan.petproject.appcore.storage.GetAllPlantsWithAnimalId
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.ModelStoragePlatformProvider
import com.cramsan.petproject.appcore.storage.implementation.DescriptionImpl
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.PlantCommonNameImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantFamilyImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantImp
import com.cramsan.petproject.appcore.storage.implementation.PlantMainNameImpl
import com.cramsan.petproject.appcore.storage.implementation.ToxicityImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.newInstance
import org.kodein.di.provider
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class ModelProviderCommonTest {

    lateinit var eventLogger: EventLoggerInterface
    lateinit var threadUtil: ThreadUtilInterface
    lateinit var configProvider: ProviderConfig
    lateinit var preferences: PreferencesInterface
    lateinit var modelStorage: ModelStorageInterface
    lateinit var modelProvider: ModelProviderInterface

    fun setUp() {
        eventLogger = mockk(relaxUnitFun = true)
        threadUtil = mockk(relaxUnitFun = true)
        configProvider = mockk(relaxUnitFun = true)
        preferences = mockk(relaxUnitFun = true)
        modelStorage = mockk()

        modelProvider = ModelProvider(eventLogger, threadUtil, modelStorage, preferences, configProvider)
    }

    suspend fun testIsCatalogAvailable() = coroutineScope {
        val now = Clock.System.now().toEpochMilliseconds()
        val systemTZ = TimeZone.currentSystemDefault()

        every { preferences.loadLong(any()) } returns null
        assertFalse(modelProvider.isCatalogAvailable(now), "Should not be available when no date was persisted")

        every { preferences.loadLong(any()) } returns Clock.System.now().plus(-25, DateTimeUnit.HOUR, systemTZ).toEpochMilliseconds()
        assertFalse(modelProvider.isCatalogAvailable(now), "Should not be available when last persisted more than 24 hours ago")

        every { preferences.loadLong(any()) } returns Clock.System.now().plus(-23, DateTimeUnit.HOUR, systemTZ).toEpochMilliseconds()
        assertFalse(modelProvider.isCatalogAvailable(now), "Should be available when persisted less than a day ago")

        modelProvider = ModelProvider(eventLogger, threadUtil, modelStorage, preferences, configProvider)
        every { preferences.loadLong(any()) } returns now
        assertTrue(modelProvider.isCatalogAvailable(now), "Should be available if just updated")
    }

    suspend fun testFiltering() = coroutineScope {
        every { preferences.loadLong(any()) } returns Clock.System.now().toEpochMilliseconds()
        every { modelStorage.getCustomPlantsEntries(any(), any()) } returns createEntries()

        modelProvider.isCatalogAvailable(Clock.System.now().toEpochMilliseconds())
        modelProvider.getPlantsWithToxicity(AnimalType.CAT, "en")

        var result = modelProvider.getPlantsWithToxicityFiltered(AnimalType.CAT, "100", "en")
        assertNotNull(result)
        assertEquals(result.size, 1)

        val job = launch {
            for (i in 0..99) {
                modelProvider.getPlantsWithToxicityFiltered(AnimalType.CAT, "100", "en")
            }
            result = modelProvider.getPlantsWithToxicityFiltered(AnimalType.CAT, "65", "en")
        }
        job.join()
        assertNotNull(result)
        assertEquals(result!!.size, 1)
    }

    private fun createEntries() = (1..100).map { i ->
        val plant = com.cramsan.petproject.appcore.storage.implementation.sqldelight.GetAllPlantsWithAnimalId(com.cramsan.petproject.db.GetAllPlantsWithAnimalId(
            i.toLong(),
            "$i",
            "https://www.aspca.org",
            AnimalType.CAT,
            ToxicityValue.TOXIC
        ))
        plant
    }.toList()
}
