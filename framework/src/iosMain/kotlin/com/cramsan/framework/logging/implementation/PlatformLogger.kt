package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.PlatformLoggerInterface
import com.cramsan.framework.logging.Severity

actual class PlatformLogger : PlatformLoggerInterface {
    override fun log(severity: Severity, tag: String, message: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
