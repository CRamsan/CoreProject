package com.cramsan.framework.base.implementation

import com.cramsan.framework.base.BaseModuleInitializerInterface
import com.cramsan.framework.base.BaseModulePlatformInitializerInterface

abstract class BaseModuleInitializer<T>(platformInitializer: BaseModulePlatformInitializerInterface<T>) : BaseModuleInitializerInterface<T>
