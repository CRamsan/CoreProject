package com.cramsan.framework.metrics

interface MetricsInterface {
    fun initialize()

    fun log(tag: String, event: String)

    fun log(tag: String, event: String, metadata: Map<String, String>)
}
