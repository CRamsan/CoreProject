package com.cramsan.framework.metrics.implementation

import com.amazonaws.services.cloudwatch.model.StandardUnit
import com.cramsan.framework.metrics.MetricUnit

fun MetricUnit.toStandardUnit(): StandardUnit = when (this) {
    MetricUnit.COUNT -> StandardUnit.Count
    MetricUnit.MILLIS -> StandardUnit.Milliseconds
    MetricUnit.SECONDS -> StandardUnit.Seconds
}
