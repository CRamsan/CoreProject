package com.cramsan.cdkrepo.remoteconfig

import com.cramsan.cdkrepo.common.PublicReadOnlyBucket
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.constructs.Construct
import java.io.File

/**
 * Stack that publishes a [remoteConfi] file to S3 to be used as a source of remote configs. The
 * file should be created by using the [initializePayload] function.
 */
class RemoteConfigStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    remoteConfig: File,
    props: StackProps? = null,
) : Stack(scope, id, props) {
    init {
        val bucket = PublicReadOnlyBucket(this, "RemoteConfigBucket")

        RemoteConfigUploader(bucket, remoteConfig, this, "Uploader")
    }
}
