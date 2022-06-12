package com.cramsan.kotlinlib.aws

import com.cramsan.cdkrepo.angular.AngularAmplify
import com.cramsan.cdkrepo.backend.ElasticBeanstalkApplication
import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps

/**
 * This stack will deploy the target application front-end and back-end configurations.
 */
class FullStackApplicationStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null,
    apply: (FullStackApplicationStack.() -> Unit)? = null,
) : Stack(scope, id, props) {
    init {
        AngularAmplify(this, "KotlinLibsFE")

        ElasticBeanstalkApplication(this, "KotlinLibsBE")

        apply?.let { it(this) }
    }
}