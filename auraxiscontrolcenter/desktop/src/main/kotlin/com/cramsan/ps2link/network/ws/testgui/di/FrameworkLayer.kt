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
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Class to initialize all the framework level components.
 */
val FrameworkModule = module {
    single<PreferencesDelegate> { ApplicationModule.providePreferencesDelegate() }

    single<Preferences> { ApplicationModule.providePreferencesInterface(get()) }

    single(named(IS_DEBUG_NAME)) { ApplicationModule.isInDebugMode(get()) }

    single<EventLoggerDelegate> {
        ApplicationModule.provideEventLoggerDelegate(get(named(IS_DEBUG_NAME)))
    }

    single<EventLoggerInterface> {
        val logger = ApplicationModule.provideEventLoggerInterface(get(), get(named(IS_DEBUG_NAME)))
        EventLogger.setInstance(logger)
        logger
    }

    single<HaltUtilDelegate> { ApplicationModule.provideHaltUtilDelegate() }

    single<HaltUtil> { ApplicationModule.provideHaltUtilInterface(get()) }

    single<AssertUtilInterface> {
        val assertUtil = ApplicationModule.provideAssertUtil(get(), get())
        AssertUtil.setInstance(assertUtil)
        assertUtil
    }

    single<ThreadUtilDelegate> { ApplicationModule.provideThreadUtilDelegate(get(), get()) }

    single<ThreadUtilInterface> {
        val threadUtil = ApplicationModule.provideThreadUtilInterface(get())
        ThreadUtil.setInstance(threadUtil)
        threadUtil
    }

    single<DispatcherProvider> { ApplicationModule.provideDispatcher() }
}

private const val IS_DEBUG_NAME = "isDebugEnabled"
