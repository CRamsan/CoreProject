package com.cramsan.framework.metrics.implementation

import software.amazon.awssdk.http.SdkHttpResponse

/**
 * Exception class that wraps around a [SdkHttpResponse].
 */
class AwsSdkException(sdkResponse: SdkHttpResponse) : Exception(sdkResponse.toString())
