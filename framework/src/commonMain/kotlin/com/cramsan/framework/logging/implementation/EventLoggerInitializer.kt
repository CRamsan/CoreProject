package com.cramsan.framework.logging.implementation

import com.cramsan.framework.base.implementation.BaseModuleInitializer
import com.cramsan.framework.logging.EventLoggerPlatformInitializerInterface
import com.cramsan.framework.logging.Severity

class EventLoggerInitializer(val platformInitializer: EventLoggerPlatformInitializerInterface,
                             val targetSeverity: Severity
) : BaseModuleInitializer<LoggerManifest>(platformInitializer)
