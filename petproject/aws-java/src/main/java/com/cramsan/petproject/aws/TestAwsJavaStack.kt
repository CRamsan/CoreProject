package com.cramsan.petproject.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Duration
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime

class TestAwsJavaStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null
) : Stack(scope, id, props) {
    init {
        Storage(this, "Storage")

        val function = Function.Builder.create(this, "api-lambda")
            .code(Code.fromAsset("../awslambda-java/build/libs/awslambda-java-all.jar"))
            .handler("com.cramsan.petproject.awslambda.LambdaHandler")
            .timeout(Duration.seconds(30))
            .memorySize(1024)
            .runtime(Runtime.JAVA_11)
            .build()

        Api(this, "Api", function)
    }
}
