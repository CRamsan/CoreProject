package com.cramsan.petproject.appcore.framework.implementation

import com.cramsan.framework.halt.HaltUtilAPI
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerAPI
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLoggerInitializer
import com.cramsan.framework.thread.ThreadUtilAPI
import com.cramsan.framework.halt.implementation.HaltUtilInitializer
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer
import com.cramsan.petproject.appcore.framework.CoreFrameworkInterface
import com.cramsan.petproject.appcore.provider.ModelProviderAPI
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderInitializer
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderPlatformInitializer
import com.cramsan.petproject.appcore.storage.ModelStorageAPI
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

internal class CoreFramework : CoreFrameworkInterface {

    // Initialize all Framework(low-level) components
    override lateinit var eventLogger: EventLoggerInterface
    override fun initEventLogger(targetSeverity: Severity) {
        val initializer = EventLoggerInitializer(targetSeverity)
        EventLoggerAPI.init(initializer)
        eventLogger = EventLoggerAPI.eventLogger
        eventLogger.log(Severity.INFO, classTag(), "initEventLogger")
    }

    override lateinit var threadUtil: ThreadUtilInterface
    override fun initThreadUtil() {
        eventLogger.log(Severity.INFO, classTag(), "initThreadUtil")
        val initializer = ThreadUtilInitializer(eventLogger)
        ThreadUtilAPI.init(initializer)
        threadUtil = ThreadUtilAPI.threadUtil
    }

    override lateinit var haltUtil: HaltUtilInterface
    override fun initHaltUtil() {
        eventLogger.log(Severity.INFO, classTag(), "initHaltUtil")
        val initializer = HaltUtilInitializer(eventLogger)
        HaltUtilAPI.init(initializer)
        haltUtil = HaltUtilAPI.haltUtil
        EventLoggerAPI.setHaltUtil(haltUtil)
    }

    // Initialize all Core(mid-level) components
    override lateinit var modelStorage: ModelStorageInterface
    override fun initModelStorage(platformInitializer: ModelStoragePlatformInitializer) {
        eventLogger.log(Severity.INFO, classTag(), "initModelStorage")
        val initializer = ModelStorageInitializer(platformInitializer)
        ModelStorageAPI.init(initializer)
        modelStorage = ModelStorageAPI.modelStorage
    }

    override lateinit var modelProvider: ModelProviderInterface
    override fun initModelProvider(platformInitializer: ModelProviderPlatformInitializer) {
        eventLogger.log(Severity.INFO, classTag(), "initModelProvider")
        val initializer = ModelProviderInitializer(platformInitializer)
        ModelProviderAPI.init(initializer)
        modelProvider = ModelProviderAPI.modelProvider
    }

}