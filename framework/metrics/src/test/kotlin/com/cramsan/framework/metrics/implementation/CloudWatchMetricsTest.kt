package com.cramsan.framework.metrics.implementation

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricUnit
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.test.TestBase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.Test

class CloudWatchMetricsTest : TestBase() {

    lateinit var cloudwatchMetrics: MetricsDelegate

    @MockK
    lateinit var client: AmazonCloudWatchClient

    @MockK(relaxed = false, relaxUnitFun = false,)
    lateinit var eventLogger: EventLoggerInterface

    override fun setupTest() {
        cloudwatchMetrics = CloudwatchMetrics(
            client,
            dispatcherProvider,
            eventLogger,
            testCoroutineScope
        )
    }

    @Test
    fun testDequeingRequests() = runBlockingTest {
        every { client.putMetricData(any()) } returns Unit

        cloudwatchMetrics.initialize()
        for (i in 1..10) {
            cloudwatchMetrics.record(MetricType.COUNT, "TestNamespace", "TAG", emptyMap(), 10.0, MetricUnit.SECONDS)
        }

        verify(exactly = 10) { client.putMetricData(any()) }
    }

    @Test
    fun testOverflowingRequests() = runBlockingTest {
        every { client.putMetricData(any()) } returns Unit

        // First fill the buffer. We expect the size to be 10 requests, so lest push more than that.
        for (i in 1..20) {
            cloudwatchMetrics.record(MetricType.COUNT, "TestNamespace", "TAG", emptyMap(), 10.0, MetricUnit.SECONDS)
        }

        // For this text we are trying to record events BEFORE initializing the instance. This should not be done
        // for other use cases. We are doing it here just to force the buffer to fill up before we start consuming requests.
        cloudwatchMetrics.initialize()

        verify(exactly = 10) { client.putMetricData(any()) }
    }
}
