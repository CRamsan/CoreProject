package com.cramsan.petproject.framework

import com.cramsan.petproject.logging.EventLoggerInterface
import com.cramsan.petproject.logging.implementation.EventLogger
import com.cramsan.petproject.logging.implementation.PlatformLogger
import com.cramsan.petproject.storage.ModelStorageInterface
import com.cramsan.petproject.storage.implementation.ModelStorage
import com.cramsan.petproject.thread.ThreadUtilInterface
import com.cramsan.petproject.thread.implementation.ThreadUtil

class CoreFramework {
    companion object {
        val eventLogger: EventLoggerInterface by lazy { EventLogger(PlatformLogger()) }
        val modelStorage: ModelStorageInterface by lazy { ModelStorage() }
        val threadUtil: ThreadUtilInterface by lazy { ThreadUtil() }
    }
}