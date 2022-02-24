package com.cramsan.framework.test

/**
 * Basic repo class to be implemented on each platform.
 */
expect class Repository() {

    suspend fun getData(): Int

    fun getDataBlocking(): Int
}
