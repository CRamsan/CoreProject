package com.cramsan.framework.test

import kotlinx.coroutines.delay

class RepositoryImpl : Repository {
    override suspend fun getData(): Int {
        delay(100)
        return 100
    }

    override fun getDataBlocking(): Int {
        Thread.sleep(100)
        return 100
    }
}
