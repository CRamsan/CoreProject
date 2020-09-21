package com.cramsan.petproject.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var modelProvider: ModelProviderInterface

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var metrics: MetricsInterface

    override suspend fun doWork(): Result = coroutineScope {
        eventLogger.log(Severity.INFO, "SyncWorker", "Starting to sync")
        val startTime = System.currentTimeMillis() / 1000L
        modelProvider.downloadCatalog(startTime, true)
        val endTime = System.currentTimeMillis() / 1000L
        val latency = (endTime - startTime).toString()
        metrics.log("SyncWorker", "syncLatency", mapOf("Latency" to latency))
        // Indicate whether the work finished successfully with the Result
        Result.success()
    }
}
