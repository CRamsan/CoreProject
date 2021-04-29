package com.cramsan.framework.halt.implementation

import android.content.Context
import com.cramsan.framework.halt.HaltUtilDelegate

class HaltUtilAndroid(private val appContext: Context) : HaltUtilDelegate {

    override fun stopThread() = Unit

    override fun resumeThread() = Unit

    override fun crashApp() = Unit
}
