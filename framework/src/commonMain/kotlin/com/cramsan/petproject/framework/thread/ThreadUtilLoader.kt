package com.cramsan.petproject.framework.thread

import com.cramsan.petproject.framework.logging.EventLoggerInterface
import com.cramsan.petproject.framework.thread.implementation.ThreadUtil

object ThreadUtilLoader {
    private var eventLogger: EventLoggerInterface? = null

    val threadUtil: ThreadUtilInterface by lazy { ThreadUtil(eventLogger!!) }

    fun init(eventLogger: EventLoggerInterface) {
        this.eventLogger = eventLogger
    }
}