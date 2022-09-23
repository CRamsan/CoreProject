package me.cesar.application.common.network

import me.cesar.application.common.model.Source

/**
 * Network model for the result of a request of data ingestion across multiple sources. The [ingestions] will contain
 * a result for each individual source.
 */
data class IngestionResult(
    val ingestions: List<SourceIngestionResult>,
)

/**
 * Network model for the result of a request of data ingestion for a single [source].
 */
data class SourceIngestionResult(
    val source: Source,
    val newArticleCount: Int,
)
