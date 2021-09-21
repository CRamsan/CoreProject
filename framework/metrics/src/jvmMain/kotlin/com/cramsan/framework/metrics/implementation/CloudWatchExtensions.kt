package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.metrics.MetricUnit
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit

fun MetricUnit.toStandardUnit(): StandardUnit = when (this) {
    MetricUnit.COUNT -> StandardUnit.COUNT
    MetricUnit.MILLIS -> StandardUnit.MILLISECONDS
    MetricUnit.SECONDS -> StandardUnit.SECONDS
}
