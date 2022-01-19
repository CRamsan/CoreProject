package com.cesarandres.ps2link.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps

class MetricsStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null
) : Stack(scope, id, props) {
    init {
        DefaultAccess(this, "DefaultAccess")
    }
}
