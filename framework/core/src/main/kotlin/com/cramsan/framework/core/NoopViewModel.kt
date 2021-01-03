package com.cramsan.framework.core

import android.app.Application
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface

class NoopViewModel constructor(
    application: Application,
    private val noopLogger: EventLoggerInterface = object : EventLoggerInterface {
        override val targetSeverity = Severity.ERROR
        override val errorCallback: EventLoggerErrorCallbackInterface? = null
        override val platformDelegate: EventLoggerDelegate = TODO()
        override fun log(severity: Severity, tag: String, message: String) = Unit
    },
    private val noopMetrics: MetricsInterface = object : MetricsInterface {
        override val platformDelegate: MetricsDelegate = TODO()
        override fun initialize() = Unit
        override fun log(tag: String, event: String) = Unit
        override fun log(tag: String, event: String, metadata: Map<String, String>) = Unit
    },
    private val noopThreadUtil: ThreadUtilInterface = object : ThreadUtilInterface {
        override val platformDelegate: ThreadUtilDelegate = TODO()
        override fun isUIThread() = false
        override fun isBackgroundThread() = false
        override fun dispatchToBackground(block: RunBlock) = Unit
        override fun dispatchToUI(block: RunBlock) = Unit
        override fun assertIsUIThread() = Unit
        override fun assertIsBackgroundThread() = Unit
    },
    private val noopAssert: AssertUtilInterface = object : AssertUtilInterface {
        override val haltOnFailure = false
        override val eventLogger: EventLoggerInterface? = null
        override val haltUtil: HaltUtilInterface? = null
        override fun assert(condition: Boolean, tag: String, message: String) = Unit
    },
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(application, noopLogger, noopMetrics, noopThreadUtil, dispatcherProvider) {
    override val logTag: String
        get() = "NoopViewModel"
}
