package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.preferences.PlatformPreferencesInterface
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.preferences.implementation.PreferencesInitializer
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import io.mockk.mockk
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.newInstance

internal class ModelProviderCommonTest {

    private val kodein = Kodein {
        bind<EventLoggerInterface>() with provider { mockk<EventLogger>(relaxUnitFun = true) }
        bind<ThreadUtilInterface>() with provider { mockk<ThreadUtil>(relaxUnitFun = true) }
        bind<PreferencesInterface>() with provider { mockk<Preferences>(relaxUnitFun = true) }
    }

    private lateinit var modelProvider: ModelProviderInterface
    private lateinit var modelProviderImpl: ModelProvider

    fun setUp(storagePlatformInitializer: ModelStoragePlatformInitializer, platformPreferencesInterface: PlatformPreferencesInterface) {
        val modelStorage by kodein.newInstance { ModelStorage(ModelStorageInitializer(storagePlatformInitializer), instance(), instance()) }
        val preferences by kodein.newInstance { Preferences(PreferencesInitializer(platformPreferencesInterface)) }
        val newModelProvider by kodein.newInstance { ModelProvider(instance(), instance(), modelStorage, instance()) }
        modelProviderImpl = newModelProvider
        modelProvider = modelProviderImpl
    }

    fun testFiltering() {
    }
}
