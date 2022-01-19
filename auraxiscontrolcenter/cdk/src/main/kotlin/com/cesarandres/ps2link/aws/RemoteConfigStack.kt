package com.cesarandres.ps2link.aws

import com.cramsan.ps2link.remoteconfig.RemoteConfigData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps
import java.io.File

class RemoteConfigStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    json: Json,
    props: StackProps? = null
) : Stack(scope, id, props) {
    init {
        val bucket = PublicReadOnlyBucket(this, "RemoteConfigBucket")

        val payload = RemoteConfigData(
            listOf("planetside2", "PS2DailyDeals", "WrelPlays")
        )

        // Directory where app was started
        val startDir = File(System.getProperty("user.dir"))
        val outputFolder = File(startDir.absolutePath + File.separator + "build" + File.separator + "remoteConfig")
        outputFolder.mkdirs()
        require(outputFolder.isDirectory)

        val serializedData = json.encodeToString(payload)
        val file = File(outputFolder.path + File.separator + "payload+$id")
        file.writeText(serializedData)

        RemoteConfigUploader(bucket, file, this, "Uploader")
    }
}
