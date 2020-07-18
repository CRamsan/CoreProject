package com.cramsan.petproject.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import kotlinx.coroutines.coroutineScope
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class SyncWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams),
    KodeinAware {

    override val kodein by kodein(appContext)

    private val modelProvider: ModelProviderInterface by instance()
    private val eventLogger: EventLoggerInterface by instance()
    private val metrics: MetricsInterface by instance()

    override suspend fun doWork(): Result = coroutineScope {
        eventLogger.log(Severity.INFO, "SyncWorker", "Starting to sync")
        val startTime = System.currentTimeMillis() / 1000L
        modelProvider.downloadCatalog(startTime)
        val endTime = System.currentTimeMillis() / 1000L
        val latency = (endTime - startTime).toString()
        metrics.log("SyncWorker", "syncLatency", mapOf("Latency" to latency))
        // Indicate whether the work finished successfully with the Result
        Result.success()
    }
}
