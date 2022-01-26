package com.cramsan.cdkrepo.common

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.s3.Bucket

/**
 * **BE CAREFUL This construct will create a resource that is publicly accessible.**
 * This construct will create an S3 bucket with Read-Only public access.
 */
class PublicReadOnlyBucket(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    /**
     * [Bucket] that will be created.
     */
    val bucket: Bucket

    init {
        bucket = Bucket(this, "Public-RO-Bucket-$id")
        bucket.grantPublicAccess(null, "s3:GetObject")
    }
}
