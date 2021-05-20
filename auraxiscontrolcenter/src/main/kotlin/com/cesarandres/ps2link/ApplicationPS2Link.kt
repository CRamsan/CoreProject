package com.cesarandres.ps2link

import android.app.Application
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.logMetric
import com.cramsan.framework.thread.ThreadUtilInterface
import com.microsoft.appcenter.AppCenter
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class ApplicationPS2Link : Application() {

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var crashHandler: CrashHandler

    @Inject
    lateinit var metrics: MetricsInterface

    @Inject
    lateinit var threadUtil: ThreadUtilInterface

    @Inject
    lateinit var haltUtil: HaltUtil

    @Inject
    lateinit var assertUtil: AssertUtilInterface

    @Inject
    @Named(PS2ApplicationModuleConstants.APP_CENTER_ID)
    lateinit var appCenterId: String

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    override fun onCreate() {
        super.onCreate()
        eventLogger.log(Severity.INFO, TAG, "onCreate called")
        AppCenter.start(this, appCenterId)
        crashHandler.initialize()
        metrics.initialize()
        // TODO: Hilt is not injecting the AssertUtils. This is a workaround for that
        assertUtil = PS2ApplicationModule.provideAssertUtil(eventLogger, haltUtil)
        logMetric(TAG, "Application Started")
    }

    companion object {
        const val TAG = "ApplicationPS2Link"
    }
}
