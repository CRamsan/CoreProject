package me.cesar.application.spring

import com.cramsan.framework.utils.uuid.UUID
import kotlinx.datetime.Instant
import me.cesar.application.common.model.Article
import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import me.cesar.application.common.network.InsertionRequest
import me.cesar.application.spring.storage.ddb.entity.ArticleEntity
import me.cesar.application.spring.storage.ddb.entity.SourceEntity

/**
 * Convert an Article model from the data layer into the domain layer.
 */
fun ArticleEntity.toModel() = Article(
    id = requireNotNull(id),
    title = title ?: "",
    sourceId = source ?: "",
    content = content ?: "",
    publishedTime = Instant.fromEpochSeconds(lastUpdated ?: 0),
)

/**
 * Convert a Source model from the data layer into the domain layer.
 */
fun SourceEntity.toModel() = Source(
    id = requireNotNull(id),
    title = title,
    url = url,
    lastUpdated = Instant.fromEpochSeconds(lastUpdated ?: 0),
    sourceType = SourceType.valueOf(sourceType ?: ""),
)

/**
 * Convert an Article model from the data layer into the domain layer.
 */
fun Article.toStorage() = ArticleEntity(
    id = id,
    title = title,
    source = sourceId,
    content = content,
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
    sourceType = sourceType.name,
)

/**
 *
 */
fun InsertionRequest.toSource() = Source(
    id = UUID.fromString("$url-${sourceType.name}"),
    title = title,
    url = url,
    sourceType = sourceType,
    lastUpdated = Instant.DISTANT_PAST,
)
