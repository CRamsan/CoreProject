package com.cramsan.framework.halt

import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilInitializer

object HaltUtilAPI {
    private lateinit var initializer: HaltUtilInitializer

    val haltUtil: HaltUtilInterface by lazy { HaltUtil(initializer) }

    fun init(initializer: HaltUtilInitializer) {
        HaltUtilAPI.initializer = initializer
    }
}