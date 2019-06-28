package com.cramsan.framework.thread

import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer

object ThreadUtilAPI {
    private lateinit var initializer: ThreadUtilInitializer

    private lateinit var internalThreadUtil: ThreadUtil
    val threadUtil: ThreadUtilInterface by lazy {
        internalThreadUtil = ThreadUtil(initializer)
        internalThreadUtil
    }

    fun init(initializer: ThreadUtilInitializer) {
        this.initializer = initializer
    }
}