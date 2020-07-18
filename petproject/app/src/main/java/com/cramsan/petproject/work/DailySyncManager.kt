package com.cramsan.petproject.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cramsan.petproject.PetProjectApplication
import java.util.concurrent.TimeUnit
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class DailySyncManager : ScheduledSyncManager, KodeinAware {

    override val kodein by lazy { PetProjectApplication.getInstance().kodein }
    val context: Context by instance()

    override fun startWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val saveRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(saveRequest)
    }
}
