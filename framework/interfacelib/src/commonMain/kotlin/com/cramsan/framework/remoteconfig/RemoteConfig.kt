package com.cramsan.framework.remoteconfig

/**
 * Interface to the remote config manager.
 *
 * This class is intended to be started when the process starts and then it will cache the downloaded values for the
 * duration of the process.
 *
 * The recommended usage is as follow:
 * - Call [downloadConfigPayload]
 * - Check the return value of [downloadConfigPayload] and handle any failures.
 * - Call [getConfigPayloadOrDefault] at any point in the app to get the config payload.
 */
interface RemoteConfig<T> {

    /**
     * Return [true] if the remote config payload has been downloaded.
     */
    fun isConfigPayloadReady(): Boolean

    /**
     * Start the download of remote config payload if it has not been downloaded already.
     * If the payload is already downloaded, this function returns immediately.
     *
     * This function will return true if the catalog was downloaded successfully, false otherwise.
     */
    suspend fun downloadConfigPayload(): Boolean

    /**
     * Similar to [downloadConfigPayload] but the operation is performed on the background.
     * This function always returns immediately.
     * @see downloadConfigPayload
     */
    fun downloadConfigPayloadAsync()

    /**
     * Instead of calling this function, it is recommended to ensure [isConfigPayloadReady] is returning true
     * and then use [getConfigPayloadOrDefault] instead.
     *
     * If [isConfigPayloadReady] returns true, then this function will return the instance of remote config payload
     * of type [T]. If [isConfigPayloadReady] returns false, this function will return null.
     *
     * @see isConfigPayloadReady
     * @see getConfigPayloadOrDefault
     */
    fun getConfigPayloadOrNull(): T?

    /**
     * If [isConfigPayloadReady] returns true, then this function will return the instance of remote config payload
     * of type [T]. If [isConfigPayloadReady] returns false, this function will return a default instance.
     * @see isConfigPayloadReady
     */
    fun getConfigPayloadOrDefault(): T
}
