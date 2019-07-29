package com.cramsan.framework.thread

import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
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
        initializer.eventLoggerInterface.log(Severity.INFO, classTag(), "init")
        this.initializer = initializer
    }
}