package com.cramsan.petproject

import android.app.Application
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.userevents.UserEventsInterface
import com.cramsan.petproject.PetProjectApplicationModule.APP_CENTER_ID
import com.cramsan.petproject.work.ScheduledSyncManager
import com.microsoft.appcenter.AppCenter
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named

/**
 * PetProject application.
 */
@HiltAndroidApp
@Suppress("UndocumentedPublicProperty")
class PetProjectApplication : Application() {

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var crashHandler: CrashHandler

    @Inject
    lateinit var userEvents: UserEventsInterface

    @Inject
    lateinit var assertUtilInterface: AssertUtilInterface

    @Inject
    lateinit var syncManager: ScheduledSyncManager

    @Inject
    @Named(APP_CENTER_ID)
    lateinit var appCenterId: String

    override fun onCreate() {
        super.onCreate()
        eventLogger.log(Severity.INFO, "PetProjectApplication", "onCreate called")
        AppCenter.start(this, appCenterId)
        crashHandler.initialize()
        userEvents.initialize()
        // syncManager.startWork()
    }
}
