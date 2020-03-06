package com.cesarandres.ps2link.module.reddit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Data_ {

    /**
     * @return The domain
     */
    /**
     * @param domain The domain
     */
    @Expose
    var domain: String? = null
    /**
     * @return The bannedBy
     */
    /**
     * @param bannedBy The banned_by
     */
    @SerializedName("banned_by")
    @Expose
    var bannedBy: Any? = null
    /**
     * @return The mediaEmbed
     */
    /**
     * @param mediaEmbed The media_embed
     */
    @SerializedName("media_embed")
    @Expose
    var mediaEmbed: MediaEmbed? = null
    /**
     * @return The subreddit
     */
    /**
     * @param subreddit The subreddit
     */
    @Expose
    var subreddit: String? = null
    /**
     * @return The selftextHtml
     */
    /**
     * @param selftextHtml The selftext_html
     */
    @SerializedName("selftext_html")
    @Expose
    var selftextHtml: String? = null
    /**
     * @return The selftext
     */
    /**
     * @param selftext The selftext
     */
    @Expose
    var selftext: String? = null
    /**
     * @return The likes
     */
    /**
     * @param likes The likes
     */
    @Expose
    var likes: Any? = null
    /**
     * @return The userReports
     */
    /**
     * @param userReports The user_reports
     */
    @SerializedName("user_reports")
    @Expose
    var userReports: List<Any> = ArrayList()
    /**
     * @return The secureMedia
     */
    /**
     * @param secureMedia The secure_media
     */
    @SerializedName("secure_media")
    @Expose
    var secureMedia: Any? = null
    /**
     * @return The linkFlairText
     */
    /**
     * @param linkFlairText The link_flair_text
     */
    @SerializedName("link_flair_text")
    @Expose
    var linkFlairText: String? = null
    /**
     * @return The id
     */
    /**
     * @param id The id
     */
    @Expose
    var id: String? = null
    /**
     * @return The gilded
     */
    /**
     * @param gilded The gilded
     */
    @Expose
    var gilded: Int? = null
    /**
     * @return The archived
     */
    /**
     * @param archived The archived
     */
    @Expose
    var archived: Boolean? = null
    /**
     * @return The clicked
     */
    /**
     * @param clicked The clicked
     */
    @Expose
    var clicked: Boolean? = null
    /**
     * @return The reportReasons
     */
    /**
     * @param reportReasons The report_reasons
     */
    @SerializedName("report_reasons")
    @Expose
    var reportReasons: Any? = null
    /**
     * @return The author
     */
    /**
     * @param author The author
     */
    @Expose
    var author: String? = null
    /**
     * @return The numComments
     */
    /**
     * @param numComments The num_comments
     */
    @SerializedName("num_comments")
    @Expose
    var numComments: Int? = null
    /**
     * @return The score
     */
    /**
     * @param score The score
     */
    @Expose
    var score: Int? = null
    /**
     * @return The approvedBy
     */
    /**
     * @param approvedBy The approved_by
     */
    @SerializedName("approved_by")
    @Expose
    var approvedBy: Any? = null
    /**
     * @return The over18
     */
    /**
     * @param over18 The over_18
     */
    @SerializedName("over_18")
    @Expose
    var over18: Boolean? = null
    /**
     * @return The hidden
     */
    /**
     * @param hidden The hidden
     */
    @Expose
    var hidden: Boolean? = null
    /**
     * @return The thumbnail
     */
    /**
     * @param thumbnail The thumbnail
     */
    @Expose
    var thumbnail: String? = null
    /**
     * @return The subredditId
     */
    /**
     * @param subredditId The subreddit_id
     */
    @SerializedName("subreddit_id")
    @Expose
    var subredditId: String? = null
    /**
     * @return The edited
     */
    /**
     * @param edited The edited
     */
    @Expose
    var edited: Any? = null
    /**
     * @return The linkFlairCssClass
     */
    /**
     * @param linkFlairCssClass The link_flair_css_class
     */
    @SerializedName("link_flair_css_class")
    @Expose
    var linkFlairCssClass: String? = null
    /**
     * @return The authorFlairCssClass
     */
    /**
     * @param authorFlairCssClass The author_flair_css_class
     */
    @SerializedName("author_flair_css_class")
    @Expose
    var authorFlairCssClass: Any? = null
    /**
     * @return The downs
     */
    /**
     * @param downs The downs
     */
    @Expose
    var downs: Int? = null
    /**
     * @return The secureMediaEmbed
     */
    /**
     * @param secureMediaEmbed The secure_media_embed
     */
    @SerializedName("secure_media_embed")
    @Expose
    var secureMediaEmbed: SecureMediaEmbed? = null
    /**
     * @return The saved
     */
    /**
     * @param saved The saved
     */
    @Expose
    var saved: Boolean? = null
    /**
     * @return The stickied
     */
    /**
     * @param stickied The stickied
     */
    @Expose
    var stickied: Boolean? = null
    /**
     * @return The isSelf
     */
    /**
     * @param isSelf The is_self
     */
    @SerializedName("is_self")
    @Expose
    var isSelf: Boolean? = null
    /**
     * @return The permalink
     */
    /**
     * @param permalink The permalink
     */
    @Expose
    var permalink: String? = null
    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    @Expose
    var name: String? = null
    /**
     * @return The created
     */
    /**
     * @param created The created
     */
    @Expose
    var created: Int? = null
    /**
     * @return The url
     */
    /**
     * @param url The url
     */
    @Expose
    var url: String? = null
    /**
     * @return The authorFlairText
     */
    /**
     * @param authorFlairText The author_flair_text
     */
    @SerializedName("author_flair_text")
    @Expose
    var authorFlairText: Any? = null
    /**
     * @return The title
     */
    /**
     * @param title The title
     */
    @Expose
    var title: String? = null
    /**
     * @return The createdUtc
     */
    /**
     * @param createdUtc The created_utc
     */
    @SerializedName("created_utc")
    @Expose
    var createdUtc: Int? = null
    /**
     * @return The distinguished
     */
    /**
     * @param distinguished The distinguished
     */
    @Expose
    var distinguished: Any? = null
    /**
     * @return The media
     */
    /**
     * @param media The media
     */
    @Expose
    var media: Any? = null
    /**
     * @return The modReports
     */
    /**
     * @param modReports The mod_reports
     */
    @SerializedName("mod_reports")
    @Expose
    var modReports: List<Any> = ArrayList()
    /**
     * @return The visited
     */
    /**
     * @param visited The visited
     */
    @Expose
    var visited: Boolean? = null
    /**
     * @return The numReports
     */
    /**
     * @param numReports The num_reports
     */
    @SerializedName("num_reports")
    @Expose
    var numReports: Any? = null
    /**
     * @return The ups
     */
    /**
     * @param ups The ups
     */
    @Expose
    var ups: Int? = null

}
