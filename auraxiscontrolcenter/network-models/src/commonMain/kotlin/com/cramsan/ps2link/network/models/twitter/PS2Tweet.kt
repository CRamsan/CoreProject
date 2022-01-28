package com.cramsan.ps2link.network.models.twitter

/**
 * This class will hold the information of a tweet.
 */
class PS2Tweet : Comparable<PS2Tweet> {
    /**
     * @return the user that created this tweet
     */
    /**
     * @param user the user that created this tweet
     */
    var user: String? = null
    /**
     * @return the content of this tweet
     */
    /**
     * @param content the content for this tweet
     */
    var content: String? = null
    /**
     * @return the alias of the user
     */
    /**
     * @param tag the alias of this user
     */
    var tag: String? = null
    var date: Long? = null
    /**
     * @return the url of the thumbnail for this tweet
     */
    /**
     * @param imgUrl the url of the thumbnail for this tweet
     */
    var imgUrl: String? = null
    /**
     * @return the unique id of this tweet
     */
    /**
     * @param id the unique identifier for this tweeet
     */
    var id: String? = null

    /**
     * url to the tweet
     */
    var sourceUrl: String? = null

    /**
     * @param id id of this tweet. This is unique for every tweet
     * @param user user that originally created or retweeted this tweet
     * @param date unix time of when this tweet was created
     * @param content text contained in this tweet
     * @param tag tag or alias of the user
     * @param imgUrl url to retrieve the image of the user
     */
    constructor(id: String, user: String, date: Long, content: String, tag: String, imgUrl: String, sourceUrl: String?) {
        this.user = user
        this.date = date
        this.content = content
        this.tag = tag
        this.id = id
        this.imgUrl = imgUrl
        this.sourceUrl = sourceUrl
    }

    /**
     * Empty constructor. None of the fields will be initialized
     */
    constructor() {}

    /**
     * @return the unix date when this tweet was created
     */
    fun getDate(): Long {
        return date!!
    }

    /**
     * @param date the unix date when this tweet was created
     */
    fun setDate(date: Long) {
        this.date = date
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    override fun compareTo(other: PS2Tweet): Int {
        return this.date!!.compareTo(other.getDate()) * -1
    }
}
