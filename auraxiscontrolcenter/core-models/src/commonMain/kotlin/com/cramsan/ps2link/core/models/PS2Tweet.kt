package com.cramsan.ps2link.core.models

/**
 * This class will hold the information of a tweet.
 */
data class PS2Tweet(
    val user: String,
    val content: String,
    val tag: String,
    val date: Long,
    val imgUrl: String,
    val id: String,
    /**
     * Url that opens the tweet in the Twitter website.
     */
    val sourceUrl: String,
)
