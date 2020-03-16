package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.base.implementation.BaseModuleInitializer
import com.cramsan.framework.metrics.MetricsPlatformInitializerInterface

class MetricsInitializer(val platformInitializer: MetricsPlatformInitializerInterface) :
    BaseModuleInitializer<MetricsManifest>(platformInitializer)
