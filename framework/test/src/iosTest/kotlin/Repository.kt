package com.cramsan.framework.test

import kotlinx.coroutines.delay
import platform.posix.sleep

/**
 * Basic repo class to be implemented on each platform.
 */
actual class Repository {
    actual suspend fun getData(): Int {
        delay(100)
        return 100
    }

    actual fun getDataBlocking(): Int {
        sleep(1)
        return 100
    }
}
