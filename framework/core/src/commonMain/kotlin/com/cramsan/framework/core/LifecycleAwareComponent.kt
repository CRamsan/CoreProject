package com.cramsan.framework.core

import kotlinx.coroutines.CoroutineScope

interface LifecycleAwareComponent {

    val scope: CoroutineScope

    fun endScope()
}
