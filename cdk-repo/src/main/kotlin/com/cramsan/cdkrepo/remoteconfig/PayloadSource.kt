package com.cramsan.cdkrepo.remoteconfig

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Function that creates a JSON file from the provided [payload].
 */
inline fun <reified T> initializePayload(json: Json, id: String, payload: T): File {
    // Directory where app was started
    val startDir = File(System.getProperty("user.dir"))
    val outputFolder = File(startDir.absolutePath + File.separator + "build" + File.separator + "remoteConfig")
    outputFolder.mkdirs()
    require(outputFolder.isDirectory)

    val serializedPayload = json.encodeToString(payload)
    val file = File(outputFolder.path + File.separator + "payload+$id")
    file.writeText(serializedPayload)
    return file
}
