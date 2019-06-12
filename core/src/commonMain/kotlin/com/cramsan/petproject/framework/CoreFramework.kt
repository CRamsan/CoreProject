package com.cramsan.petproject.framework

import com.cramsan.petproject.framework.logging.EventLoggerInterface
import com.cramsan.petproject.framework.logging.EventLoggerLoader
import com.cramsan.petproject.storage.ModelStorageInterface
import com.cramsan.petproject.storage.implementation.ModelStorage
import com.cramsan.petproject.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.framework.thread.ThreadUtilLoader

class CoreFramework {
    companion object {
        val eventLogger: EventLoggerInterface by lazy { EventLoggerLoader.eventLogger }
        val threadUtil: ThreadUtilInterface by lazy {
            ThreadUtilLoader.init(eventLogger)
            ThreadUtilLoader.threadUtil
        }
        val modelStorage: ModelStorageInterface by lazy { ModelStorage() }
    }
}