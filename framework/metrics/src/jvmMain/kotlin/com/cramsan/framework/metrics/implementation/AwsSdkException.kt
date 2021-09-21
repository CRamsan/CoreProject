package com.cramsan.framework.metrics.implementation

import software.amazon.awssdk.http.SdkHttpResponse

class AwsSdkException(sdkResponse: SdkHttpResponse) : Exception(sdkResponse.toString())
