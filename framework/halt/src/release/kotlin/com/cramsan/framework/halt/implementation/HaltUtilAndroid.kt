package com.cramsan.framework.halt.implementation

import android.content.Context
import com.cramsan.framework.halt.HaltUtilDelegate

/**
 * Release implementation of [HaltUtilDelegate].As this class is intended for a release target, it
 * is a noop class.
 */
class HaltUtilAndroid(private val appContext: Context) : HaltUtilDelegate {

    override fun stopThread() = Unit

    override fun resumeThread() = Unit

    override fun crashApp() = Unit
}
