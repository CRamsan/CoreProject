package com.cramsan.framework.metrics

/**
 * Define the type of event that is being recorded.
 */
enum class MetricType {
    /**
     * Example: Record number of items in the queue.
     */
    COUNT,
    /**
     * Example: Record latency of a network request.
     */
    LATENCY,
    /**
     * Example: Record that app was launched.
     */
    EVENT,
    /**
     * To be used in combination with [FAILURE].
     * Example: Record that file was uploaded successfully.
     */
    SUCCESS,
    /**
     * To be used in combination with [SUCCESS].
     * Example: Record that file upload failed.
     */
    FAILURE,
}
