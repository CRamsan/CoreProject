package com.cramsan.framework.http

import kotlin.reflect.KClass

interface HttpEngine {

    fun <T> get(url: String, callback: (T))

    fun <T> getBlocking(url: String): T

    suspend fun <T : Any> get(url: String, c: KClass<T>): T
}
