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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CloudWatchMetricsTest : TestBase() {

    lateinit var cloudwatchMetrics: MetricsDelegate

    // We need an extra scope because the CloudWatchMetrics instance will start collection from a SharedFlow that will
    // never end. The only way to close it is by closing the scope it belongs to.
    lateinit var internalIoScope: TestScope

    @MockK
    lateinit var client: AmazonCloudWatchClient

    @MockK(relaxed = false, relaxUnitFun = false,)
    lateinit var eventLogger: EventLoggerInterface

    override fun setupTest() {
        internalIoScope = TestScope()

        cloudwatchMetrics = CloudwatchMetrics(
            client,
            dispatcherProvider,
            eventLogger,
            internalIoScope,
        )
    }

    @Test
    fun testDequeingRequests() = runBlockingTest {
        every { client.putMetricData(any()) } returns Unit

        cloudwatchMetrics.initialize()
        for (i in 1..10) {
            cloudwatchMetrics.record(MetricType.COUNT, "TestNamespace", "TAG", emptyMap(), 10.0, MetricUnit.SECONDS)
        }

        internalIoScope.advanceTimeBy(1000)

        verify(exactly = 10) { client.putMetricData(any()) }

        internalIoScope.cancel()
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

        internalIoScope.advanceTimeBy(1000)

        verify(exactly = 10) { client.putMetricData(any()) }

        internalIoScope.cancel()
    }
}
