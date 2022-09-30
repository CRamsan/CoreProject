package me.cesar.application.spring.storage.mongodb.entity

import me.cesar.application.common.model.SourceType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Entity class to persist a source in the database.
 * The source represents a location that can be accessed to retrieve new content.
 *
 * @author cramsan
 */
@Document("source")
data class SourceEntity(
    @Id
    val id: String,
    val title: String,
    val url: String,
    val lastUpdated: Long,
    val sourceType: SourceType,
)
