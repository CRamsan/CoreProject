package com.cramsan.cdkrepo.metrics

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps

/**
 * This stack will deploy resources for client-side metrics collection.
 */
class MetricsStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null
) : Stack(scope, id, props) {
    init {
        MetricsDefaultAccess(this, "DefaultAccess")
    }
}
