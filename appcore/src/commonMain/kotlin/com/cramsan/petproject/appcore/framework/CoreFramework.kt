package com.cramsan.petproject.appcore.framework

import com.cramsan.framework.logging.EventLoggerAPI
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilAPI
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer
import com.cramsan.petproject.appcore.storage.ModelStorageAPI
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

object CoreFramework {

    // Initialize all Framework(low-level) components
    lateinit var eventLogger: EventLoggerInterface
    private set
    fun initEventLogger() {
        eventLogger = EventLoggerAPI.eventLogger
    }

    lateinit var threadUtil: ThreadUtilInterface
    private set
    fun initThreadUtil() {
        val initializer = ThreadUtilInitializer(eventLogger)
        ThreadUtilAPI.init(initializer)
        threadUtil = ThreadUtilAPI.threadUtil
    }

    // Initialize all Core(mid-level) components
    lateinit var modelStorage: ModelStorageInterface
    private set
    fun initModelStorage(platformInitializer: ModelStoragePlatformInitializer) {
        val initializer = ModelStorageInitializer(platformInitializer)
        ModelStorageAPI.init(initializer)
        modelStorage = ModelStorageAPI.modelStorage
    }
}