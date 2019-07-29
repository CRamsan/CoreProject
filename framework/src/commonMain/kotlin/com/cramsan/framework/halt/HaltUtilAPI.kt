package com.cramsan.framework.halt

import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilInitializer
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag

object HaltUtilAPI {
    private lateinit var initializer: HaltUtilInitializer

    val haltUtil: HaltUtilInterface by lazy { HaltUtil(initializer) }

    fun init(initializer: HaltUtilInitializer) {
        initializer.eventLoggerInterface.log(Severity.INFO, classTag(), "init")
        HaltUtilAPI.initializer = initializer
    }
}