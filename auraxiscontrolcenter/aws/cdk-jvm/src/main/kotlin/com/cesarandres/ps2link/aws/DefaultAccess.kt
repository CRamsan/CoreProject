package com.cesarandres.ps2link.aws

import software.amazon.awscdk.core.CfnOutput
import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.iam.CfnAccessKey
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.User

class DefaultAccess(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    init {
        val readOnlyUser = User(this, "ReadOnly", null)
        readOnlyUser.addManagedPolicy(
            ManagedPolicy.fromAwsManagedPolicyName("CloudWatchReadOnlyAccess")
        )

        val cloudWatchMetricAccess = User(this, "CloudWatchMetricAccess")
        cloudWatchMetricAccess.addManagedPolicy(
            ManagedPolicy.fromAwsManagedPolicyName("CloudWatchFullAccess")
        )
        val accessKey = CfnAccessKey(this, "apiAccess") {
            cloudWatchMetricAccess.userName
        }

        CfnOutput(this, "cloudWatchMetricAccess_AccessToken") { accessKey.ref }
        CfnOutput(this, "cloudWatchMetricAccess_SecretAccessToken") { accessKey.attrSecretAccessKey }
    }
}
