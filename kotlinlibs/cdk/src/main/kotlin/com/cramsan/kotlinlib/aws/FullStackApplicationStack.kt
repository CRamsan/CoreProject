package com.cramsan.kotlinlib.aws

import com.cramsan.cdkrepo.angular.AngularAmplify
import com.cramsan.cdkrepo.backend.ElasticBeanstalkApplication
import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.events.Rule
import software.amazon.awscdk.services.events.RuleProps
import software.amazon.awscdk.services.events.Schedule
import software.amazon.awscdk.services.events.targets.LambdaFunction
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime
import software.constructs.Construct

/**
 * This stack will deploy the target application front-end and back-end configurations.
 */
class FullStackApplicationStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null,
    scheduleExpression: String,
    apply: (FullStackApplicationStack.() -> Unit)? = null,
) : Stack(scope, id, props) {
    init {
        AngularAmplify(this, "KotlinLibsFE")

        ElasticBeanstalkApplication(this, "KotlinLibsBE", "../server-jvm/build/libs/server-jvm-all.jar")

        apply?.let { it(this) }

        val function = Function.Builder.create(this, "periodic-handler")
            // This is how we would deploy the code through CDK. In our case, we will deploy
            // though a CICD pipeline.
            .code(Code.fromAsset("../lambda-jvm/build/libs/lambda-jvm-all.jar"))
            .handler("me.cesar.application.lambda.LambdaHandler")
            .timeout(Duration.seconds(30))
            .memorySize(1024)
            .runtime(Runtime.JAVA_11)
            .build()

        Rule(
            this,
            "lambda-periodic",
            RuleProps.builder()
                .apply {
                    enabled(true)
                    schedule(Schedule.expression(scheduleExpression))
                    targets(mutableListOf(LambdaFunction(function)))
                }
                .build(),
        )
    }
}
