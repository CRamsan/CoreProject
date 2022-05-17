package me.cesar.application.service

import kotlinx.datetime.Instant
import me.cesar.application.storage.Article

/**
 * Convert an Article model from the data layer into the domain layer.
 */
fun Article.toModel() = me.cesar.application.model.Article(
    title = title,
    source = source,
    content = content,
    publishedAt = Instant.fromEpochSeconds(publishedAt.time),
    id = id ?: 0,
)
