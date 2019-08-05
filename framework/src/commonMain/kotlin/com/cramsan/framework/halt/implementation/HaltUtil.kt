package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface

expect class HaltUtil(
    initializer: HaltUtilInitializer,
    eventLogger: EventLoggerInterface
) : HaltUtilInterface
