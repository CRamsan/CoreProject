package com.cramsan.framework.logging

import com.cramsan.framework.base.BaseModulePlatformInitializerInterface
import com.cramsan.framework.logging.implementation.LoggerManifest

interface EventLoggerPlatformInitializerInterface : BaseModulePlatformInitializerInterface<LoggerManifest> {
    val platformLogger: EventLoggerInterface
}
