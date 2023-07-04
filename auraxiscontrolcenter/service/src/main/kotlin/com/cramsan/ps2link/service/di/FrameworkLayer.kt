package com.cramsan.ps2link.service.di

import com.cramsan.framework.assertlib.AssertUtil
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.JVMDispatcherProvider
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerJVM
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.implementation.JVMPreferencesDelegate
import com.cramsan.framework.preferences.implementation.PreferencesImpl
import com.cramsan.framework.thread.ThreadUtil
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.framework.thread.implementation.ThreadUtilJVM
import org.koin.dsl.module

/**
 * Class to initialize all the framework level components.
 */
val FrameworkModule = module {
    single<PreferencesDelegate> {
        JVMPreferencesDelegate()
    }

    single<Preferences> {
        PreferencesImpl(get())
    }

    single<EventLoggerDelegate> {
        LoggerJVM(false)
    }

    single<EventLoggerInterface> {
        val instance = EventLoggerImpl(Severity.INFO, null, get())
        EventLogger.setInstance(instance)
        instance
    }

    single<AssertUtilInterface> {
        val impl = AssertUtilImpl(
            false,
            get(),
            null,
        )
        AssertUtil.setInstance(impl)
        impl
    }

    single<ThreadUtilDelegate> {
        ThreadUtilJVM(get(), get())
    }

    single<ThreadUtilInterface> {
        val instance = ThreadUtilImpl(get())
        ThreadUtil.setInstance(instance)
        instance
    }

    single<DispatcherProvider> { JVMDispatcherProvider() }
}
