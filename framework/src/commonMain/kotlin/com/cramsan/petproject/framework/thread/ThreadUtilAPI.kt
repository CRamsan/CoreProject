package com.cramsan.petproject.framework.thread

import com.cramsan.petproject.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.framework.thread.implementation.ThreadUtilInitializer

object ThreadUtilAPI {
    private lateinit var initializer: ThreadUtilInitializer

    val threadUtil: ThreadUtilInterface by lazy { ThreadUtil(initializer) }

    fun init(initializer: ThreadUtilInitializer) {
        this.initializer = initializer
    }
}