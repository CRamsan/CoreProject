package com.cramsan.petproject

import android.app.Application
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.petproject.work.ScheduledSyncManager
import com.microsoft.appcenter.AppCenter
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PetProjectApplication : Application() {

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var crashHandler: CrashHandler

    @Inject
    lateinit var metrics: MetricsInterface

    @Inject
    lateinit var syncManager: ScheduledSyncManager

    override fun onCreate() {
        super.onCreate()
        internalInstance = this
        eventLogger.log(Severity.INFO, "PetProjectApplication", "onCreate called")
        AppCenter.start(this, "1206f21f-1b20-483f-9385-9b8cbc0e504d")
        crashHandler.initialize()
        metrics.initialize()
        // syncManager.startWork()
    }

    companion object {
        private lateinit var internalInstance: PetProjectApplication
        fun getInstance(): PetProjectApplication = internalInstance
    }
}
