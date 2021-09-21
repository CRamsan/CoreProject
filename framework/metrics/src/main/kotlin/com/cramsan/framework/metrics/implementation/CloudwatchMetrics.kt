package com.cramsan.framework.metrics.implementation

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.cloudwatch.model.Dimension
import com.amazonaws.services.cloudwatch.model.InternalServiceException
import com.amazonaws.services.cloudwatch.model.InvalidParameterCombinationException
import com.amazonaws.services.cloudwatch.model.InvalidParameterValueException
import com.amazonaws.services.cloudwatch.model.MetricDatum
import com.amazonaws.services.cloudwatch.model.MissingRequiredParameterException
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest
import com.cramsan.framework.logging.logE
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricUnit
import com.cramsan.framework.metrics.MetricsDelegate

/**
 * Implementation of [MetricsDelegate] that reports metrics to AWS Cloudwatch.
 */
class CloudwatchMetrics(
    private val accessKey: String,
    private val secretKey: String,
) : MetricsDelegate {

    lateinit var client: AmazonCloudWatchClient

    override fun initialize() {
        val awsCreds = BasicAWSCredentials(accessKey, secretKey)
        client = AmazonCloudWatchClient(awsCreds).apply {
            setRegion(Region.getRegion("us-west-2"))
        }
    }

    override fun record(
        type: MetricType,
        namespace: String,
        tag: String,
        metadata: Map<String, String>?,
        value: Double,
        unit: MetricUnit,
    ) {
        val dimensions = (
            metadata?.map {
                Dimension().apply {
                    withName(it.key)
                    withValue(it.value)
                }
            } ?: emptyList()
            ) + Dimension().apply {
            withName(IDENTIFIER)
            withValue(tag)
        }
        val metricDatum = MetricDatum().apply {
            withMetricName(type.name)
            withValue(value)
            withUnit(unit.toStandardUnit())
            withDimensions(dimensions)
        }
        val request = PutMetricDataRequest().apply {
            withMetricData(metricDatum)
            withNamespace(namespace)
        }
        try {
            client.putMetricData(request)
        } catch (throwable: Throwable) {
            when (throwable) {
                is InvalidParameterValueException, is MissingRequiredParameterException,
                is InvalidParameterCombinationException, is InternalServiceException,
                is AmazonClientException -> logE(TAG, "AWS client failure while uploading metric", throwable)
                is AmazonServiceException -> logE(TAG, "AWS service failure while uploading metric", throwable)
                else -> logE(TAG, "Undetermined failure while uploading metric", throwable)
            }
        }
    }

    companion object {
        const val IDENTIFIER = "IDENTIFIER"
        const val TAG = "CloudwatchMetrics"
    }
}
