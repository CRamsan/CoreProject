package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.MetricsPlatformInitializerInterface

class AppCenterMetricsInitializer(override val platformMetrics: MetricsInterface) : MetricsPlatformInitializerInterface
