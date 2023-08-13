package com.cramsan.framework.sample.jvm

import com.cramsan.framework.assertlib.AssertUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class FrameworkInitializer : KoinComponent {

    fun initialize() {
        AssertUtil.setInstance(get())
    }
}
