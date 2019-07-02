package com.cramsan.petproject.appcore.framework.implementation

import com.cramsan.framework.halt.HaltUtilAPI
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerAPI
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.implementation.EventLoggerInitializer
import com.cramsan.framework.thread.ThreadUtilAPI
import com.cramsan.framework.halt.implementation.HaltUtilInitializer
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer
import com.cramsan.petproject.appcore.framework.CoreFrameworkInterface
import com.cramsan.petproject.appcore.storage.ModelStorageAPI
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

class MockCoreFramework : CoreFrameworkInterface {

    override lateinit var eventLogger: EventLoggerInterface
    override fun initEventLogger() {
        val initializer = EventLoggerInitializer()
        EventLoggerAPI.init(initializer)
        eventLogger = EventLoggerAPI.eventLogger
    }

    override lateinit var threadUtil: ThreadUtilInterface
    override fun initThreadUtil() {
        val initializer = ThreadUtilInitializer(eventLogger)
        ThreadUtilAPI.init(initializer)
        threadUtil = ThreadUtilAPI.threadUtil
    }

    override lateinit var haltUtil: HaltUtilInterface
    override fun initHaltUtil() {
        val initializer = HaltUtilInitializer(eventLogger)
        HaltUtilAPI.init(initializer)
        haltUtil = HaltUtilAPI.haltUtil
        EventLoggerAPI.setHaltUtil(haltUtil)
    }

    override lateinit var modelStorage: ModelStorageInterface
    override fun initModelStorage(platformInitializer: ModelStoragePlatformInitializer) {
        val initializer = ModelStorageInitializer(platformInitializer)
        ModelStorageAPI.init(initializer)
        modelStorage = ModelStorageAPI.modelStorage
    }
}