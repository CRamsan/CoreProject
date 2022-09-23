package me.cesar.application.common.model

import kotlinx.datetime.Instant

/**
 * Domain model for representing the information about a single article.
 *
 * @author cramsan
 */
data class Article(
    var id: String,
    var title: String,
    var sourceId: String,
    var content: String,
    var publishedTime: Instant,
)
