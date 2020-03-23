package com.cramsan.framework.assert

import com.cramsan.framework.assert.implementation.AssertUtilManifest
import com.cramsan.framework.base.BaseModuleInterface

interface AssertUtilInterface : BaseModuleInterface<AssertUtilManifest> {
    fun assert(condition: Boolean, tag: String, message: String)
}
