package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
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
        bind<ModelStorageInterface>() with provider { mockk<ModelStorage>(relaxUnitFun = true) }
    }

    private lateinit var modelProvider: ModelProviderInterface
    private lateinit var modelProviderImpl: ModelProvider

    fun setUp(platformInitializer: ModelProviderPlatformInitializer) {
        val initializer = ModelProviderInitializer(platformInitializer)
        val newModelProvider by kodein.newInstance {  ModelProvider(initializer, instance(), instance(), instance()) }
        modelProviderImpl = newModelProvider
        modelProvider = modelProviderImpl
    }

    fun testNoop() {

    }

    fun tearDown() {

    }
}
