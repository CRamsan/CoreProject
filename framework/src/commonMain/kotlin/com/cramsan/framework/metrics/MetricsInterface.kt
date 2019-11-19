package com.cramsan.framework.metrics

interface MetricsInterface {
    fun initialize()

    fun log(event: String)
}