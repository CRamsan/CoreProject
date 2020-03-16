package com.cramsan.framework.metrics

import com.cramsan.framework.base.BaseModulePlatformInitializerInterface
import com.cramsan.framework.metrics.implementation.MetricsManifest

interface MetricsPlatformInitializerInterface : BaseModulePlatformInitializerInterface<MetricsManifest>{
    val platformMetrics: MetricsInterface
}