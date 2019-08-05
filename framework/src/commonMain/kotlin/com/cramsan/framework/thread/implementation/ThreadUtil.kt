package com.cramsan.framework.thread.implementation

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilInterface

expect class ThreadUtil(
    initializer: ThreadUtilInitializer,
    eventLogger: EventLoggerInterface
) : ThreadUtilInterface
