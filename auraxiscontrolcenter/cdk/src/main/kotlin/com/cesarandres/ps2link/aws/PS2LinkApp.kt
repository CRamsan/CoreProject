package com.cesarandres.ps2link.aws

import com.cramsan.cdkrepo.metrics.MetricsStack
import com.cramsan.cdkrepo.remoteconfig.RemoteConfigStack
import com.cramsan.cdkrepo.remoteconfig.initializePayload
import com.cramsan.framework.metrics.MetricType
import com.cramsan.ps2link.metric.ApplicationNamespace
import com.cramsan.ps2link.remoteconfig.remoteConfigJson
import com.cramsan.ps2link.remoteconfig.remoteConfigPayload
import software.amazon.awscdk.core.App
import software.amazon.awscdk.core.Duration
import software.amazon.awscdk.core.StackProps
import software.amazon.awscdk.services.cloudwatch.Alarm
import software.amazon.awscdk.services.cloudwatch.AlarmProps
import software.amazon.awscdk.services.cloudwatch.ComparisonOperator
import software.amazon.awscdk.services.cloudwatch.Metric
import software.amazon.awscdk.services.cloudwatch.MetricProps
import software.amazon.awscdk.services.cloudwatch.Statistic
import software.amazon.awscdk.services.cloudwatch.actions.SnsAction
import software.amazon.awscdk.services.sns.Topic
import software.amazon.awscdk.services.sns.subscriptions.EmailSubscription

object PS2LinkApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val app = App()
        val props = StackProps.builder() // If you don't specify 'env', this stack will be environment-agnostic.
            // Account/Region-dependent features and context lookups will not work,
            // but a single synthesized template can be deployed anywhere.
            // Uncomment the next block to specialize this stack for the AWS Account
            // and Region that are implied by the current CLI configuration.
            /*
            .env(Environment.builder()
                    .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                    .region(System.getenv("CDK_DEFAULT_REGION"))
                    .build())
            */
            // Uncomment the next block if you know exactly what Account and Region you
            // want to deploy the stack to.
            /*
            .env(Environment.builder()
                    .account("123456789012")
                    .region("us-east-1")
                    .build())
            */
            // For more information, see https://docs.aws.amazon.com/cdk/latest/guide/environments.html
            .build()

        MetricsStack(
            app,
            "PS2LinkApp",
            props,
        ) {
            val emailTopic = Topic(this, "emailComms").apply {
                addSubscription(EmailSubscription("alerts@cramsan.com"))
            }

            val launchMetric = Metric(
                MetricProps.builder()
                    .namespace(ApplicationNamespace.identifier)
                    .metricName(MetricType.SUCCESS.name)
                    .statistic(Statistic.SUM.name)
                    .period(Duration.hours(6))
                    .dimensionsMap(
                        mapOf(
                            "IDENTIFIER" to ApplicationNamespace.Event.LAUNCH.name
                        )
                    )
                    .build()
            )
            Alarm(
                this, "LaunchUnderflow",
                AlarmProps.builder()
                    .metric(launchMetric)
                    .comparisonOperator(ComparisonOperator.LESS_THAN_THRESHOLD)
                    .threshold(4.0)
                    .evaluationPeriods(2)
                    .datapointsToAlarm(2)
                    .build()
            ).apply {
                addAlarmAction(SnsAction(emailTopic))
            }

            val crashMetric = Metric(
                MetricProps.builder()
                    .namespace(ApplicationNamespace.identifier)
                    .metricName(MetricType.FAILURE.name)
                    .statistic(Statistic.SUM.name)
                    .period(Duration.hours(6))
                    .dimensionsMap(
                        mapOf(
                            "IDENTIFIER" to ApplicationNamespace.Event.LAUNCH.name
                        )
                    )
                    .build()
            )

            Alarm(
                this, "LaunchCrash",
                AlarmProps.builder()
                    .metric(crashMetric)
                    .comparisonOperator(ComparisonOperator.GREATER_THAN_THRESHOLD)
                    .threshold(5.0)
                    .evaluationPeriods(1)
                    .datapointsToAlarm(1)
                    .build()
            ).apply {
                addAlarmAction(SnsAction(emailTopic))
            }
        }

        val payload = initializePayload(remoteConfigJson, "ps2Link", remoteConfigPayload)
        RemoteConfigStack(
            app,
            "PS2LinkRemoteConfig",
            payload,
            props,
        )
        app.synth()
    }
}
