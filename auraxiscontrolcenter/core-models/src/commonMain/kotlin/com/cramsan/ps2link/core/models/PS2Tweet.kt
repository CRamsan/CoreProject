package com.cramsan.ps2link.core.models

/**
 * This class will hold the information of a tweet.
 */
data class PS2Tweet(
    var user: String? = null,
    var content: String? = null,
    var tag: String? = null,
    var date: Int? = null,
    var imgUrl: String? = null,
    var id: String? = null,
)
