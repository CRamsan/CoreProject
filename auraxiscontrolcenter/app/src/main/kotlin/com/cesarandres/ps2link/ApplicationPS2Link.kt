package com.cesarandres.ps2link

import android.app.Application
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.userevents.UserEventsInterface
import com.cramsan.framework.userevents.logEvent
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
    lateinit var userEvents: UserEventsInterface

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
        userEvents.initialize()
        logEvent(TAG, "Application Started")
    }

    companion object {
        const val TAG = "ApplicationPS2Link"
    }
}
