package com.cesarandres.ps2link.dbg.util

import android.util.Log

/**
 * Just a wrapper over the Log class provided by android
 */
object Logger {

    /**
     * @param level   logging level
     * @param tag     will be used as a tag for the message
     * @param message actual message
     */
    fun log(level: Int, tag: Any, message: String) {
        Logger.log(level, tag.javaClass.name, message)
    }

    /**
     * @param level   logging level
     * @param tag     will be used as a tag for the message
     * @param message actual message
     */
    fun log(level: Int, tag: String, message: String) {
        when (level) {
            Log.INFO -> Log.i(tag, message)
            Log.ASSERT -> Log.wtf(tag, message)
            Log.ERROR -> Log.e(tag, message)
            Log.VERBOSE -> Log.v(tag, message)
            Log.DEBUG -> Log.d(tag, message)
            Log.WARN -> {
                Log.w(tag, message)
                Log.w(tag, "EXCEPECTED DEBUG LEVEL:-$message")
            }
            else -> Log.w(tag, "EXCEPECTED DEBUG LEVEL:-$message")
        }
    }

}
