package com.cramsan.framework.sample.jvm

import com.cramsan.framework.sample.jvm.assertions.AssertViewModel
import com.cramsan.framework.sample.jvm.eventlogger.EventLoggerViewModel
import org.koin.dsl.module

val ViewModelModule = module {
    single<AssertViewModel> {
        AssertViewModel()
    }

    single<EventLoggerViewModel> {
        EventLoggerViewModel(get(), get())
    }
}
