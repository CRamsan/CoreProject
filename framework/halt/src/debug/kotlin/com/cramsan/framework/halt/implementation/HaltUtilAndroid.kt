package com.cramsan.framework.halt.implementation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.R

/**
 * [HaltUtilDelegate] implementation for the debug target.
 */
class HaltUtilAndroid(private val appContext: Context) : HaltUtilDelegate {

    private var shouldStop = true

    override fun stopThread() {
        shouldStop = true

        displayNotification()

        while (shouldStop) {
            Thread.sleep(sleepTime)
        }
    }

    override fun resumeThread() {
        shouldStop = false
    }

    override fun crashApp() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    private fun getStacktrace() =
        Thread.currentThread().stackTrace
            .drop(STACK_TRACE_HEAD_EXTRA_LINES)
            .joinToString(separator = "\n") { "at ${it.methodName}(${it.fileName}:${it.lineNumber})" }

    private fun displayNotification() {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setContentTitle("Process has been halted")
            .setSmallIcon(R.drawable.debug_icon)
            .setContentText(getStacktrace())
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getStacktrace())
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
        with(NotificationManagerCompat.from(appContext)) {
            // NotificationId is a unique int for each notification that you must define
            // Setting notification ID to MAX_VALUE as to reduce the risk
            // of collision with other actual notification ids.
            notify(Int.MAX_VALUE, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = appContext.getString(R.string.halt_util_channel_name)
            val descriptionText = appContext.getString(R.string.halt_util_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val STACK_TRACE_HEAD_EXTRA_LINES = 90
        private const val sleepTime = 1000L
        private const val CHANNEL_ID = "com.cramsan.framework.halt.implementation"
    }
}
