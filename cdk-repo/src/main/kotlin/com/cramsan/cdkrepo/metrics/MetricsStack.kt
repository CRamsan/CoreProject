package com.cramsan.cdkrepo.metrics

import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.constructs.Construct

/**
 * This stack will deploy resources for client-side metrics collection.
 */
class MetricsStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null,
    apply: (MetricsStack.() -> Unit)? = null,
) : Stack(scope, id, props) {
    init {
        MetricsDefaultAccess(this, "DefaultAccess")
        apply?.let { it() }
    }
}
