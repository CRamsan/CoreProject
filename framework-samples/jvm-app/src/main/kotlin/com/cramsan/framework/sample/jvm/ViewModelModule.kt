package com.cramsan.framework.sample.jvm

import com.cramsan.framework.sample.jvm.assert.AssertViewModel
import org.koin.dsl.module

val ViewModelModule = module {
    single<AssertViewModel> {
        AssertViewModel()
    }
}
