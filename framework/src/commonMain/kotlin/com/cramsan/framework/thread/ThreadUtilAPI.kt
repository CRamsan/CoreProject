package com.cramsan.framework.thread

import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer

object ThreadUtilAPI {
    private lateinit var initializer: ThreadUtilInitializer

    val threadUtil: ThreadUtilInterface by lazy { ThreadUtil(initializer) }

    fun init(initializer: ThreadUtilInitializer) {
        this.initializer = initializer
    }
}