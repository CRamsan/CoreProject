package com.cesarandres.ps2link.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.s3.Bucket

class PublicReadOnlyBucket(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    val bucket: Bucket

    init {
        bucket = Bucket(this, "Public-RO-Bucket-$id")
        bucket.grantPublicAccess(null, "s3:GetObject")
    }
}
