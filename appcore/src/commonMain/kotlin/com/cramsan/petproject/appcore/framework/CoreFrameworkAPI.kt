package com.cramsan.petproject.appcore.framework

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.framework.implementation.CoreFramework
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderPlatformInitializer
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

object CoreFrameworkAPI : CoreFrameworkInterface {

    private lateinit var coreFramework: CoreFrameworkInterface

    fun init() {
        init(CoreFramework())
    }

    fun init(coreFrameworkImp: CoreFrameworkInterface) {
        coreFramework = coreFrameworkImp
    }

    override val eventLogger: EventLoggerInterface
        get() = coreFramework.eventLogger

    override fun initEventLogger() {
        coreFramework.initEventLogger()
    }

    override val threadUtil: ThreadUtilInterface
        get() = coreFramework.threadUtil

    override fun initThreadUtil() {
        coreFramework.initThreadUtil()
    }

    override val haltUtil: HaltUtilInterface
        get() = coreFramework.haltUtil

    override fun initHaltUtil() {
        coreFramework.initHaltUtil()
    }

    override val modelStorage: ModelStorageInterface
        get() = coreFramework.modelStorage

    override fun initModelStorage(platformInitializer: ModelStoragePlatformInitializer) {
        coreFramework.initModelStorage(platformInitializer)
    }

    override val modelProvider: ModelProviderInterface
        get() = coreFramework.modelProvider

    override fun initModelProvider(platformInitializer: ModelProviderPlatformInitializer) {
        coreFramework.initModelProvider(platformInitializer)
    }
}