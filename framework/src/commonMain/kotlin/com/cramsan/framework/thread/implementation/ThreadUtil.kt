package com.cramsan.framework.thread.implementation

import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilInterface

expect class ThreadUtil(
    eventLogger: EventLoggerInterface,
    assertUtil: AssertUtilInterface
) : ThreadUtilInterface
