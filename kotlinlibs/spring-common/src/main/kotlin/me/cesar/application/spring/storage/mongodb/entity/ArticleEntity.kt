package me.cesar.application.spring.storage.mongodb.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Entity for storing the information about a single article.
 *
 * @author cramsan
 */
@Document("article")
data class ArticleEntity(
    @Id
    val id: String,
    val title: String,
    val sourceId: String,
    val content: String,
    val bannerUrl: String,
    val lastUpdated: Long,
)
