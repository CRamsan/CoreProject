package me.cesar.application.spring.storage.mongodb

import kotlinx.datetime.Instant
import me.cesar.application.common.model.Article
import me.cesar.application.common.model.Source
import me.cesar.application.spring.storage.mongodb.entity.ArticleEntity
import me.cesar.application.spring.storage.mongodb.entity.SourceEntity

/**
 * Convert an Article model from the data layer into the domain layer.
 */
fun ArticleEntity.toModel() = Article(
    id = id,
    title = title,
    sourceId = sourceId,
    content = content,
    bannerUrl = bannerUrl,
    publishedTime = Instant.fromEpochSeconds(lastUpdated),
)

/**
 * Convert a Source model from the data layer into the domain layer.
 */
fun SourceEntity.toModel() = Source(
    id = id,
    title = title,
    url = url,
    lastUpdated = Instant.fromEpochSeconds(lastUpdated),
    sourceType = sourceType,
)

/**
 * Convert an Article model from the data layer into the domain layer.
 */
fun Article.toStorage() = ArticleEntity(
    id = id,
    title = title,
    sourceId = sourceId,
    content = content,
    bannerUrl = bannerUrl,
    lastUpdated = publishedTime.epochSeconds,
)

/**
 * Convert a Source model from the data layer into the domain layer.
 */
fun Source.toStorage() = SourceEntity(
    id = id,
    title = title,
    url = url,
    lastUpdated = lastUpdated.epochSeconds,
    sourceType = sourceType,
)
