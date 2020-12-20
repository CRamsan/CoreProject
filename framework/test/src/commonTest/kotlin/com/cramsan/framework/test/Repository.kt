package com.cramsan.framework.test

/**
 * Basic repo class to be implemented on each platform.
 */
interface Repository {

    suspend fun getData(): Int

    fun getDataBlocking(): Int
}
