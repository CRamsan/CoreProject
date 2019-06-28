package com.cramsan.framework.logging

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.EventLoggerInitializer
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil

object EventLoggerAPI {
    private lateinit var initializer: EventLoggerInitializer

    private lateinit var internalEventLogger: EventLogger
    val eventLogger: EventLoggerInterface by lazy {
        internalEventLogger = EventLogger(initializer)
        internalEventLogger
    }

    fun init(initializer: EventLoggerInitializer) {
        this.initializer = initializer
    }

    fun setHaltUtil(haltUtil: HaltUtilInterface) {
        internalEventLogger.haltUtil = haltUtil
    }
}