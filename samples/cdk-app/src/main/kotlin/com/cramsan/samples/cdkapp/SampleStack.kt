package com.cramsan.samples.cdkapp

import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.constructs.Construct

/**
 * This stack will deploy resources for client-side metrics collection.
 */
class SampleStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null,
    apply: (SampleStack.() -> Unit)? = null,
) : Stack(scope, id, props) {
    init {
        apply?.let { it() }
    }
}
