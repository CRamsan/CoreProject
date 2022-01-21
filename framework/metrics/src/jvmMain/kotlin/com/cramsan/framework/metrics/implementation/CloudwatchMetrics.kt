package com.cramsan.framework.metrics.implementation

import com.cramsan.framework.logging.logE
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricUnit
import com.cramsan.framework.metrics.MetricsDelegate
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.core.exception.SdkClientException
import software.amazon.awssdk.core.exception.SdkException
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException
import software.amazon.awssdk.services.cloudwatch.model.Dimension
import software.amazon.awssdk.services.cloudwatch.model.InternalServiceException
import software.amazon.awssdk.services.cloudwatch.model.InvalidParameterCombinationException
import software.amazon.awssdk.services.cloudwatch.model.InvalidParameterValueException
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum
import software.amazon.awssdk.services.cloudwatch.model.MissingRequiredParameterException
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest

/**
 * Implementation of [MetricsDelegate] that reports metrics to AWS Cloudwatch.
 */
class CloudwatchMetrics(
    private val accessKey: String,
    private val secretKey: String,
) : MetricsDelegate {

    private lateinit var client: CloudWatchClient

    override fun initialize() {
        val awsCreds = AwsBasicCredentials.create(accessKey, secretKey)
        client = CloudWatchClient.builder().apply {
            region(Region.US_WEST_2)
            credentialsProvider { awsCreds }
        }.build()
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
                Dimension.builder().name(it.key).value(it.value).build()
            } ?: emptyList()
            ) + Dimension.builder().name(IDENTIFIER).value(tag).build()
        val metricDatum = MetricDatum.builder().apply {
            metricName(type.name)
            value(value)
            unit(unit.toStandardUnit())
            dimensions(dimensions)
        }.build()
        val request = PutMetricDataRequest.builder().apply {
            metricData(metricDatum)
            namespace(namespace)
        }.build()
        try {
            val response = client.putMetricData(request)
            if (!response.sdkHttpResponse().isSuccessful) {
                logE(TAG, "Failed to upload metric", AwsSdkException(response.sdkHttpResponse()))
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is InvalidParameterValueException, is MissingRequiredParameterException,
                is InvalidParameterCombinationException -> logE(TAG, "Failed to upload metric due to bad request", throwable)
                is InternalServiceException -> logE(TAG, "Failed to upload metric due to service error", throwable)
                is SdkClientException -> logE(TAG, "Failed to upload metric due to generic client-side issue", throwable)
                is CloudWatchException -> logE(TAG, "Failed to upload metric due to generic CW issue", throwable)
                is SdkException -> logE(TAG, "Failed to upload metric due to generic SDK issue", throwable)
            }
        }
    }

    companion object {
        private const val IDENTIFIER = "IDENTIFIER"
        private const val TAG = "CloudwatchMetrics"
    }
}
