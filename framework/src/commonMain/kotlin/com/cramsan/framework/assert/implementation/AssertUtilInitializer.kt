package com.cramsan.framework.assert.implementation

import com.cramsan.framework.base.implementation.BaseModuleInitializer

class AssertUtilInitializer(val haltOnFailure: Boolean) :
    BaseModuleInitializer<AssertUtilManifest>(AssertUtilNoopPlatformInitializer())
