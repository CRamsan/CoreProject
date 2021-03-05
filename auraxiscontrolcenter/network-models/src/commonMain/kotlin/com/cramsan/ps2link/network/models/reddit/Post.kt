package com.cramsan.ps2link.network.models.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val selftext: String? = null,
    val mod_reason_title: String? = null,
    val gilded: Int? = 0,
    val title: String? = null,
    val downs: Int? = 0,
    val ups: Int? = 0,
    val total_awards_received: Int? = 0,
    val created: Double? = null,
    val created_utc: Double? = null,
    val over_18: Boolean? = false,
    val author: String? = null,
    val num_comments: Int? = 0,
    val stickied: Boolean? = false,
    val url: String? = null,
)
