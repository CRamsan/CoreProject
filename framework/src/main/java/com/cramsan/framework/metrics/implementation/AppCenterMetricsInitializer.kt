package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.MetricsPlatformInitializerInterface
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics

class AppCenterMetricsInitializer(override val platformMetrics: MetricsInterface) : MetricsPlatformInitializerInterface