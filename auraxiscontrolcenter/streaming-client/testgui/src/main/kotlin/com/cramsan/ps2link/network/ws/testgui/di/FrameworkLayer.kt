package com.cramsan.ps2link.network.ws.testgui.di

import com.cramsan.framework.assertlib.AssertUtil
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.thread.ThreadUtil
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.ps2link.network.ws.testgui.ApplicationModule

/**
 * Class to initialize all the framework level components.
 */
class FrameworkLayer {

    val loggerDelegate: EventLoggerDelegate

    val logger: EventLoggerInterface

    val haltUtilDelegate: HaltUtilDelegate

    val haltUtil: HaltUtil

    val assertUtil: AssertUtilInterface

    val threadUtilDelegate: ThreadUtilDelegate

    val threadUtil: ThreadUtilInterface

    val preferencesDelegate: PreferencesDelegate

    val preferences: Preferences

    val dispatcherProvider: DispatcherProvider

    init {
        preferencesDelegate = ApplicationModule.providePreferencesDelegate()
        preferences = ApplicationModule.providePreferencesInterface(preferencesDelegate)

        val isDebugEnabled = ApplicationModule.isInDebugMode(preferences)
        loggerDelegate = ApplicationModule.provideEventLoggerDelegate(isDebugEnabled)
        logger = ApplicationModule.provideEventLoggerInterface(loggerDelegate, isDebugEnabled)
        EventLogger.setInstance(logger)

        haltUtilDelegate = ApplicationModule.provideHaltUtilDelegate()
        haltUtil = ApplicationModule.provideHaltUtilInterface(haltUtilDelegate)

        assertUtil = ApplicationModule.provideAssertUtil(logger, haltUtil)
        AssertUtil.setInstance(assertUtil)

        threadUtilDelegate = ApplicationModule.provideThreadUtilDelegate(logger, assertUtil)
        threadUtil = ApplicationModule.provideThreadUtilInterface(threadUtilDelegate)
        ThreadUtil.setInstance(threadUtil)

        dispatcherProvider = ApplicationModule.provideDispatcher()
    }
}
