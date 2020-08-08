package com.cramsan.petproject.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cramsan.petproject.PetProjectApplication
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.concurrent.TimeUnit

class DailySyncManager : ScheduledSyncManager, DIAware {

    override val di by lazy { PetProjectApplication.getInstance().di }
    val context: Context by instance()

    override fun startWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val saveRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(saveRequest)
    }
}
