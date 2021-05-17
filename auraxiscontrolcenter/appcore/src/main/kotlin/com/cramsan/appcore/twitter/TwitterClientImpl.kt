package com.cramsan.appcore.twitter

import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.cramsan.ps2link.appcore.twitter.TwitterUser
import com.cramsan.ps2link.core.models.PS2Tweet
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.util.ArrayList

/**
 * This class is used to retrieve tweets from the requested users. The public
 * methods will take care of configuring the connection with the API
 */
class TwitterClientImpl(
    private val CONSUMER_SECRET: String,
    private val CONSUMER_KEY: String,
    private val ACCESS_TOKEN: String,
    private val ACCESS_TOKEN_SECRET: String,
) : TwitterClient {

    private val twitter: Twitter by lazy { configureTwitter() }

    /**
     * @param users an array with all the users to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    override fun getTweets(users: List<TwitterUser>): List<PS2Tweet> {
        return retrieveTweets(users.map { it.handle })
    }

    /**
     * @param users the user to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    override fun getTweets(user: TwitterUser): List<PS2Tweet> {
        val twitterUser = listOf(user.handle)
        return retrieveTweets(twitterUser)
    }

    /**
     * @return the twitter object after being configured with the parameters to
     * contact the API
     */
    private fun configureTwitter(): Twitter {
        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY)
            .setOAuthConsumerSecret(CONSUMER_SECRET).setOAuthAccessToken(ACCESS_TOKEN)
            .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
        val tf = TwitterFactory(cb.build())
        return tf.instance
    }

    /**
     * @param twitter a configured Twitter object
     * @param users a list of users to retrieve tweets from
     * @return an arraylist with all the tweets found
     * @throws TwitterException this exception is thrown where there is an error
     * communicating with the twitter API
     */
    private fun retrieveTweets(users: List<String>): List<PS2Tweet> {
        if (users.isEmpty()) {
            return emptyList()
        }
        val usersFound = twitter.lookupUsers(*users.toTypedArray())
        val tweetsFound = ArrayList<com.cramsan.ps2link.network.models.twitter.PS2Tweet>()
        for (foundUser in usersFound) {
            if (foundUser.status != null) {
                val statusess = twitter.getUserTimeline(foundUser.screenName)
                var name: String
                var tag: String
                var imgUrl: String
                var text: String
                for (status3 in statusess) {
                    if (status3.isRetweet || status3.isRetweetedByMe) {
                        name = status3.retweetedStatus.user.name
                        tag = status3.retweetedStatus.user.screenName
                        imgUrl = status3.retweetedStatus.user.biggerProfileImageURLHttps
                        text = status3.text + "\nRetweeted by " + status3.user.screenName
                    } else {
                        name = status3.user.name
                        tag = foundUser.screenName
                        imgUrl = status3.user.biggerProfileImageURLHttps
                        text = status3.text
                    }
                    tweetsFound.add(
                        com.cramsan.ps2link.network.models.twitter.PS2Tweet(
                            java.lang.Long.toString(status3.id),
                            name,
                            status3.createdAt.time,
                            text,
                            tag,
                            imgUrl
                        )
                    )
                }
            }
        }
        return tweetsFound.map { it.toCoreModel() }
    }
}
