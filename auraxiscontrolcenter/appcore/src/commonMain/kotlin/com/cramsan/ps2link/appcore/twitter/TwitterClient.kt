package com.cramsan.ps2link.appcore.twitter

import com.cramsan.ps2link.core.models.PS2Tweet

/**
 * @Author cramsan
 * @created 2/9/2021
 */
interface TwitterClient {
    /**
     * @param users an array with all the users to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    fun getTweets(users: List<TwitterUser>): List<PS2Tweet>

    /**
     * @param users the user to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    fun getTweets(user: TwitterUser): List<PS2Tweet>
}
