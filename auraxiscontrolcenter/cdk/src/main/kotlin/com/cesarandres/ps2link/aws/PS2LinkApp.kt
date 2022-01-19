package com.cesarandres.ps2link.aws

import com.cramsan.cdkrepo.metrics.MetricsStack
import com.cramsan.cdkrepo.remoteconfig.RemoteConfigStack
import com.cramsan.cdkrepo.remoteconfig.initializePayload
import com.cramsan.ps2link.remoteconfig.remoteConfigJson
import com.cramsan.ps2link.remoteconfig.remoteConfigPayload
import software.amazon.awscdk.core.App
import software.amazon.awscdk.core.StackProps

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
        )

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
