package com.cramsan.framework.sample.android

import android.app.Application
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.userevents.UserEventsInterface
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SampleApplication : Application() {

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var crashHandler: CrashHandler

    @Inject
    lateinit var userEvents: UserEventsInterface

    @Inject
    lateinit var threadUtil: ThreadUtilInterface

    @Inject
    lateinit var haltUtil: HaltUtil

    @Inject
    lateinit var assertUtil: AssertUtilInterface

    @Inject
    lateinit var metrics: MetricsInterface

    @Inject
    lateinit var errorCallback: EventLoggerErrorCallback

    override fun onCreate() {
        super.onCreate()

        assertUtil
        eventLogger
    }
}
