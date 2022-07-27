package com.cramsan.cdkrepo.metrics

import software.amazon.awscdk.CfnOutput
import software.amazon.awscdk.services.iam.CfnAccessKey
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.User
import software.constructs.Construct

/**
 * Construct that will create the required users and API keys to have safe and controlled access
 * to CloudWatch resources.
 *
 * There will be the following users:
 *  - ReadOnly: User with read-only access to all cloudwath resources.
 *  - CloudWatchMetricAccess: This user will have full access to CloudWatch. Intended for admin use only.
 *
 * Additionally a set of API keys will be created. The access token and secret will be output during the deployment
 * and can be viewed in the logs.
 */
class MetricsDefaultAccess(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    init {
        val readOnlyUser = User(this, "ReadOnly", null)
        readOnlyUser.addManagedPolicy(
            ManagedPolicy.fromAwsManagedPolicyName("CloudWatchReadOnlyAccess"),
        )

        val cloudWatchMetricAccess = User(this, "CloudWatchMetricAccess")
        cloudWatchMetricAccess.addManagedPolicy(
            ManagedPolicy.fromAwsManagedPolicyName("CloudWatchFullAccess"),
        )
        val accessKey = CfnAccessKey(this, "apiAccess") {
            cloudWatchMetricAccess.userName
        }

        CfnOutput(this, "cloudWatchMetricAccess_AccessToken") { accessKey.ref }
        CfnOutput(this, "cloudWatchMetricAccess_SecretAccessToken") { accessKey.attrSecretAccessKey }
    }
}
