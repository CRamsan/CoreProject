package com.cramsan.petproject.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cramsan.framework.logging.logI
import com.cramsan.framework.userevents.logEvent
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var modelProvider: ModelProviderInterface

    override suspend fun doWork(): Result = coroutineScope {
        logI("SyncWorker", "Starting to sync")
        val startTime = System.currentTimeMillis()
        modelProvider.downloadCatalog(startTime, true)
        val endTime = System.currentTimeMillis()
        val latency = (endTime - startTime).toString()
        logEvent("SyncWorker", "syncLatency", mapOf("Latency" to latency))
        // Indicate whether the work finished successfully with the Result
        Result.success()
    }
}
