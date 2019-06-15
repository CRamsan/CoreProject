package com.cramsan.petproject.framework.logging

import com.cramsan.petproject.framework.logging.implementation.EventLogger
import com.cramsan.petproject.framework.logging.implementation.PlatformLogger

object EventLoggerAPI {
    val eventLogger: EventLoggerInterface by lazy { EventLogger(PlatformLogger()) }
}