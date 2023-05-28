package com.cramsan.framework.metrics.implementation

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.cloudwatch.model.StandardUnit
import com.cramsan.framework.metrics.MetricUnit

/**
 * Function to convert from our own [MetricUnit] to AWS's [StandardUnit].
 * Not all [StandardUnit] are supported.
 */
fun MetricUnit.toStandardUnit(): StandardUnit = when (this) {
    MetricUnit.COUNT -> StandardUnit.Count
    MetricUnit.MILLIS -> StandardUnit.Milliseconds
    MetricUnit.SECONDS -> StandardUnit.Seconds
}

/**
 * Instantiate a [AmazonCloudWatchClient] by using the [accessKey] and [secretKey].
 * This function does not provide options to further configure the client before creation.
 */
fun createCloudWatchClient(accessKey: String, secretKey: String): AmazonCloudWatchClient {
    val awsCreds = BasicAWSCredentials(accessKey, secretKey)
    return AmazonCloudWatchClient(awsCreds).apply {
        setRegion(Region.getRegion("us-west-2"))
    }
}
