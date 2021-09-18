package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.ProviderConfig
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class ModelProviderCommonTest {

    lateinit var eventLogger: EventLoggerInterface
    lateinit var threadUtil: ThreadUtilInterface
    lateinit var configProvider: ProviderConfig
    lateinit var preferences: Preferences
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

        every { preferences.loadLong(any()) } returns Clock.System.now().plus(-31, DateTimeUnit.DAY, systemTZ).toEpochMilliseconds()
        assertFalse(modelProvider.isCatalogAvailable(now), "Should not be available when last persisted more than 30 days ago")

        every { preferences.loadLong(any()) } returns Clock.System.now().plus(-29, DateTimeUnit.HOUR, systemTZ).toEpochMilliseconds()
        assertTrue(modelProvider.isCatalogAvailable(now), "Should be available when persisted less than 29 days")

        modelProvider = ModelProvider(eventLogger, threadUtil, modelStorage, preferences, configProvider)
        every { preferences.loadLong(any()) } returns Clock.System.now().plus(-23, DateTimeUnit.HOUR, systemTZ).toEpochMilliseconds()
        assertTrue(modelProvider.isCatalogAvailable(now), "Should be available when persisted less than a day ago")

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
        assertEquals(result.size, 1)
    }

    private fun createEntries() = (1..100).map { i ->
        val plant = com.cramsan.petproject.appcore.storage.implementation.sqldelight.GetAllPlantsWithAnimalId(
            com.cramsan.petproject.db.GetAllPlantsWithAnimalId(
                i.toLong(),
                "$i",
                "https://www.aspca.org",
                AnimalType.CAT,
                ToxicityValue.TOXIC
            )
        )
        plant
    }.toList()
}
