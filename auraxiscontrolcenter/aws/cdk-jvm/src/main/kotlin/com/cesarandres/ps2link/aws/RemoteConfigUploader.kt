package com.cesarandres.ps2link.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.s3.assets.AssetOptions
import software.amazon.awscdk.services.s3.deployment.BucketDeployment
import software.amazon.awscdk.services.s3.deployment.BucketDeploymentProps
import software.amazon.awscdk.services.s3.deployment.Source
import java.io.File

class RemoteConfigUploader(
    bucket: PublicReadOnlyBucket,
    file: File,
    scope: software.constructs.Construct,
    id: String
) : Construct(scope, id) {

    init {
        val asset = Source.asset(
            file.parentFile.absolutePath,
            AssetOptions.builder()
                .exclude(
                    listOf(
                        "**",
                        "!${file.name}"
                    )
                )
                .build()
        )

        BucketDeployment(
            this,
            "RemoteConfig-$id",
            BucketDeploymentProps.builder()
                .sources(listOf(asset))
                .destinationBucket(bucket.bucket)
                .build()
        )
    }
}
