package com.cramsan.framework.test

import kotlinx.coroutines.delay

actual class Repository {
    actual suspend fun getData(): Int {
        delay(100)
        return 100
    }

    actual fun getDataBlocking(): Int {
        return 100
    }
}
