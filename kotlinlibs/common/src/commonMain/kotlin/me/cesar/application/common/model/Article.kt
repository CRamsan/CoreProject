package me.cesar.application.common.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.cesar.application.common.InstantAsLongSerializer

/**
 * Domain model for representing the information about a single article.
 *
 * @author cramsan
 */
@Serializable
data class Article(
    var id: String,
    var title: String,
    var sourceId: String,
    var content: String,
    val bannerUrl: String,
    @Serializable(with = InstantAsLongSerializer::class)
    var publishedTime: Instant,
)
