package com.cramsan.cdkrepo.remoteconfig

import com.cramsan.cdkrepo.common.PublicReadOnlyBucket
import software.amazon.awscdk.services.s3.assets.AssetOptions
import software.amazon.awscdk.services.s3.deployment.BucketDeployment
import software.amazon.awscdk.services.s3.deployment.BucketDeploymentProps
import software.amazon.awscdk.services.s3.deployment.Source
import software.constructs.Construct
import java.io.File

/**
 * Upload a local file [payloadFile] to a target S3 bucket created referenced in [targetBucket].
 * This construct uses the Asset capabilities of CDK and therefore it needs an AWS account that
 * has been bootstrapped.
 */
class RemoteConfigUploader(
    targetBucket: PublicReadOnlyBucket,
    payloadFile: File,
    scope: software.constructs.Construct,
    id: String,
) : Construct(scope, id) {

    init {
        val asset = Source.asset(
            payloadFile.parentFile.absolutePath,
            AssetOptions.builder()
                .exclude(
                    listOf(
                        "**",
                        "!${payloadFile.name}",
                    ),
                )
                .build(),
        )

        BucketDeployment(
            this,
            "RemoteConfig-$id",
            BucketDeploymentProps.builder()
                .sources(listOf(asset))
                .destinationBucket(targetBucket.bucket)
                .build(),
        )
    }
}
