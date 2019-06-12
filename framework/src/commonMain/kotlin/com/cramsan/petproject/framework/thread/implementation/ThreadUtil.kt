package com.cramsan.petproject.framework.thread.implementation

import com.cramsan.petproject.framework.logging.EventLoggerInterface
import com.cramsan.petproject.framework.thread.ThreadUtilInterface

internal expect class ThreadUtil(eventLogger: EventLoggerInterface) : ThreadUtilInterface