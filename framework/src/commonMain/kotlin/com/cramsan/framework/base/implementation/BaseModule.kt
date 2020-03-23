package com.cramsan.framework.base.implementation

import com.cramsan.framework.base.BaseModuleInitializerInterface
import com.cramsan.framework.base.BaseModuleInterface

abstract class BaseModule<T>(initializer: BaseModuleInitializerInterface<T>) : BaseModuleInterface<T>
