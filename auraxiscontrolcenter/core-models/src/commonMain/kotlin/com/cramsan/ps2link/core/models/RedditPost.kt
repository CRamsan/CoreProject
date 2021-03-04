package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/22/2021
 */
data class RedditPost(
    val url: String,
    val title: String,
    val imgUr: String,
    val author: String,
    val label: String?,
    val upvotes: Int,
    val comments: Int,
    val createdTime: Long,
)
