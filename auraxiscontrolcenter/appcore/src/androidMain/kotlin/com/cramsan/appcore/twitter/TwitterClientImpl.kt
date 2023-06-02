package com.cramsan.appcore.twitter

import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.appcore.network.process
import com.cramsan.ps2link.appcore.toCoreModel
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.cramsan.ps2link.core.models.PS2Tweet
import twitter4j.Twitter
import twitter4j.TwitterException
import java.time.ZoneOffset
import java.util.ArrayList

/**
 * This class is used to retrieve tweets from the requested users. The public
 * methods will take care of configuring the connection with the API
 */
class TwitterClientImpl(
    private val consumerSecret: String,
    private val consumerKey: String,
    private val accessToken: String,
    private val accessTokenSecret: String,
) : TwitterClient {

    private val twitter: Twitter by lazy { configureTwitter() }

    /**
     * @param users an array with all the users to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    override fun getTweets(users: List<String>): PS2HttpResponse<List<PS2Tweet>> {
        return retrieveTweets(users)
    }

    /**
     * @param users the user to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    override fun getTweets(user: String): PS2HttpResponse<List<PS2Tweet>> {
        val twitterUser = listOf(user)
        return retrieveTweets(twitterUser)
    }

    /**
     * @return the twitter object after being configured with the parameters to
     * contact the API
     */
    private fun configureTwitter(): Twitter {
        return Twitter.newBuilder().apply {
            oAuthConsumer(consumerKey, consumerSecret)
            oAuthAccessToken(accessToken, accessTokenSecret)
        }.build()
    }

    /**
     * @param twitter a configured Twitter object
     * @param users a list of users to retrieve tweets from
     * @return an arraylist with all the tweets found
     * @throws TwitterException this exception is thrown where there is an error
     * communicating with the twitter API
     */
    @Suppress("NestedBlockDepth")
    private fun retrieveTweets(users: List<String>): PS2HttpResponse<List<PS2Tweet>> {
        if (users.isEmpty()) {
            return PS2HttpResponse.success(emptyList())
        }
        try {
            val usersFound = twitter.v1().users().lookupUsers(*users.toTypedArray())
            val tweetsFound = ArrayList<com.cramsan.ps2link.network.models.twitter.PS2Tweet>()

            for (foundUser in usersFound) {
                if (foundUser.status != null) {
                    val statusess = twitter.v1().timelines().getUserTimeline(foundUser.screenName)
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
                        val tweetUrl = "https://twitter.com/${status3.user.screenName}/status/${status3.id}"
                        tweetsFound.add(
                            com.cramsan.ps2link.network.models.twitter.PS2Tweet(
                                java.lang.Long.toString(status3.id),
                                name,
                                status3.createdAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
                                text,
                                tag,
                                imgUrl,
                                tweetUrl,
                            ),
                        )
                    }
                }
            }
            return PS2HttpResponse.success(tweetsFound).process {
                tweetsFound.map { tweet -> tweet.toCoreModel() }
            }
        } catch (throwable: Throwable) {
            return PS2HttpResponse.failure(null, throwable)
        }
    }
}