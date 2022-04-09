package me.cesar.application.model

import kotlinx.datetime.Instant

/**
 * Domain model for representing the information about a single article.
 *
 * @author cramsan
 */
class Article(
    var title: String,
    var source: String,
    var content: String,
    var publishedAt: Instant,
    var id: Long,
)
