package com.cesarandres.ps2link.module

import android.content.Context
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

import com.cramsan.framework.thread.ThreadUtilInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance
import java.io.File
import java.util.*

class CacheManager(val context: Context) : KodeinAware {

    override val kodein by kodein(context)
    private val eventLogger: EventLoggerInterface by instance()
    private val threadUtil: ThreadUtilInterface by instance()

    /**
     * This AsyncTask will delete all the files from the cache.
     */
    suspend fun clearCache() = withContext(Dispatchers.IO) {
        eventLogger.log(Severity.INFO, TAG, "Starting to clear cache")
        val fileList = context.fileList()
        for (file in fileList) {
            context.deleteFile(file)
        }
    }

    /**
     * This AsyncTask will delete the least used files from the cache until it reaches its maximum size allowed.
     */
    suspend fun trimCache() =  withContext(Dispatchers.IO){
        eventLogger.log(Severity.INFO, TAG, "Starting to trim cache")
        threadUtil.assertIsBackgroundThread()
        val files = context.filesDir.listFiles()

        Arrays.sort(files!!) { f1, f2 ->
            java.lang.Long.valueOf(f1.lastModified()).compareTo(f2.lastModified())
        }

        var i = 0
        while (getCacheSize() > MAX_CACHE_SIZE && i < files.size) {
            context.deleteFile(files[i].absolutePath)
            i++
        }
    }

    /**
     * @return the size in bytes of all the files in the cache
     */
    suspend fun getCacheSize(): Long = withContext(Dispatchers.IO){
        val fileList = context.fileList()
        var size: Long = 0
        for (file in fileList) {
            size += File(file).length()
        }
        return@withContext size
    }

    companion object {
        const val MAX_CACHE_SIZE = 5000000 //5 MB
        private const val TAG = "CacheManager"
    }
}