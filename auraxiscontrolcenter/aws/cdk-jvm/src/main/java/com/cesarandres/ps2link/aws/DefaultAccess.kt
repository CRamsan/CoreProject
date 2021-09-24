package com.cesarandres.ps2link.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.iam.CfnAccessKey
import software.amazon.awscdk.services.iam.ManagedPolicy
import software.amazon.awscdk.services.iam.User
import software.amazon.awscdk.services.iam.UserProps
import software.amazon.awscdk.services.secretsmanager.Secret

class DefaultAccess(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    init {
        val secret = Secret(this, "readOnlyUser")
        val readOnlyUser = User(
            this,
            "ReadOnly",
            UserProps.builder()
                .password(secret.secretValue)
                .passwordResetRequired(true)
                .build()
        )
        secret.grantRead(readOnlyUser)
        readOnlyUser.addManagedPolicy(
            ManagedPolicy.fromAwsManagedPolicyName("CloudWatchReadOnlyAccess")
        )

        val cloudWatchMetricAccess = User(this, "CloudWatchMetricAccess")
        cloudWatchMetricAccess.addManagedPolicy(
            ManagedPolicy.fromAwsManagedPolicyName("CloudWatchFullAccess")
        )
        CfnAccessKey(this, "apiAccess") {
            cloudWatchMetricAccess.userName
        }
    }
}
