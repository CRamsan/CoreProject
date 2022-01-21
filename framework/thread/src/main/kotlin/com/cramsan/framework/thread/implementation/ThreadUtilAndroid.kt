package com.cramsan.framework.thread.implementation

import android.os.Handler
import android.os.Looper
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.thread.ThreadUtilDelegate

/**
 * Android implementation of [ThreadUtilDelegate].
 *
 * @see [ThreadUtilDelegate]
 */
class ThreadUtilAndroid constructor(
    private val assertUtil: AssertUtilInterface
) : ThreadUtilDelegate {

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun isUIThread(): Boolean {
        return (Looper.myLooper() == mainHandler.looper)
    }

    override fun isBackgroundThread(): Boolean {
        return !isUIThread()
    }

    override fun assertIsUIThread() {
        assertUtil.assert(isUIThread(), "ThreadUtilAndroid", "Not on UI thread!")
    }

    override fun assertIsBackgroundThread() {
        assertUtil.assert(isBackgroundThread(), "ThreadUtilAndroid", "Not on background thread!")
    }
}
