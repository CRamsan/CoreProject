package com.cramsan.petproject.awslambda

import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerJS
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.framework.thread.implementation.ThreadUtilJS
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage

class DependenciesConfig {

    private val eventLogger by lazy {
        EventLoggerImpl(Severity.INFO, null, LoggerJS())
    }

    private val assertUtil by lazy {
        AssertUtilImpl(false, eventLogger, null)
    }

    private val threadUtil by lazy {
        ThreadUtilImpl(ThreadUtilJS(eventLogger, assertUtil))
    }

    val modelStorage by lazy {
        ModelStorage(
            null!!,
            eventLogger,
            threadUtil
        )
    }
}
