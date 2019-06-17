package com.cramsan.framework.logging

import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.PlatformLogger

object EventLoggerAPI {
    val eventLogger: EventLoggerInterface by lazy { EventLogger(PlatformLogger()) }
}