package com.cramsan.framework.metrics.implementation

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.cloudwatch.model.Dimension
import com.amazonaws.services.cloudwatch.model.InternalServiceException
import com.amazonaws.services.cloudwatch.model.InvalidParameterCombinationException
import com.amazonaws.services.cloudwatch.model.InvalidParameterValueException
import com.amazonaws.services.cloudwatch.model.MetricDatum
import com.amazonaws.services.cloudwatch.model.MissingRequiredParameterException
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricUnit
import com.cramsan.framework.metrics.MetricsDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Implementation of [MetricsDelegate] that reports metrics to AWS Cloudwatch.
 * Needs the [accessKey] and [secretKey] to be able to authenticate to AWS.
 * The [dispatcherProvider] is needed so we can dispatch the work to the background
 * [DispatcherProvider.ioDispatcher]. The work will be scoped within the provided [scope].
 */
class CloudwatchMetrics(
    private val client: AmazonCloudWatchClient,
    private val dispatcherProvider: DispatcherProvider,
    private val eventLogger: EventLoggerInterface,
    private val scope: CoroutineScope,
) : MetricsDelegate {

    private val uploadRequestBuffer = MutableSharedFlow<PutMetricDataRequest>(
        replay = BUFFER_CAPACITY,
        extraBufferCapacity = BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun initialize() {
        uploadRequestBuffer.onEach { dequeueUploadRecord(it) }
            .flowOn(dispatcherProvider.ioDispatcher())
            .launchIn(scope)
    }

    override fun record(
        type: MetricType,
        namespace: String,
        tag: String,
        metadata: Map<String, String>?,
        value: Double,
        unit: MetricUnit,
    ) {
        queueUploadRecord(type, namespace, tag, metadata, value, unit)
    }

    /**
     * Method that performs the actual call to upload metrics. This method will ensure to dispatch
     * the work into the [scope] and using the [DispatcherProvider.ioDispatcher].
     */
    private fun queueUploadRecord(
        type: MetricType,
        namespace: String,
        tag: String,
        metadata: Map<String, String>?,
        value: Double,
        unit: MetricUnit,
    ) = scope.launch(dispatcherProvider.ioDispatcher()) {
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
        uploadRequestBuffer.emit(request)
    }

    /**
     * This function should also be called when in the IO Dispathcer.
     */
    private fun dequeueUploadRecord(putMetricDataRequest: PutMetricDataRequest): Boolean {
        return try {
            client.putMetricData(putMetricDataRequest)
            true
        } catch (throwable: Throwable) {
            when (throwable) {
                is InvalidParameterValueException, is MissingRequiredParameterException,
                is InvalidParameterCombinationException, is InternalServiceException,
                is AmazonServiceException -> eventLogger.log(Severity.WARNING, TAG, "AWS service failure while uploading metric", throwable, false)
                is AmazonClientException -> eventLogger.log(Severity.WARNING, TAG, "AWS client failure while uploading metric", throwable, false)
                else -> eventLogger.log(Severity.WARNING, TAG, "Undetermined failure while uploading metric", throwable, false)
            }
            false
        }
    }

    companion object {
        private const val IDENTIFIER = "IDENTIFIER"
        private const val TAG = "CloudwatchMetrics"
        private const val BUFFER_CAPACITY = 10

        /**
         * Convenience method so that the caller can create a [MetricsDelegate] without having to require them to
         * have the AWS SDK in their classpath. This function is intentionally as simple as possible and it should
         * just proxy to other call.
         *
         * If for some reason we need to implement more complicated logic, then it should not be added here and instead
         * we should look for a different solution.
         */
        fun createInstance(
            accessKey: String,
            secretKey: String,
            dispatcherProvider: DispatcherProvider,
            eventLogger: EventLoggerInterface,
            scope: CoroutineScope,
        ): MetricsDelegate {
            val client = createCloudWatchClient(accessKey, secretKey)
            return CloudwatchMetrics(
                client, dispatcherProvider, eventLogger, scope
            )
        }
    }
}
