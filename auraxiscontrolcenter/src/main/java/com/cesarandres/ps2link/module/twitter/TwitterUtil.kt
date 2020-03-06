package com.cesarandres.ps2link.module.twitter

import java.util.ArrayList

import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.conf.ConfigurationBuilder

/**
 * This class is used to retrieve tweets from the requested users. The public
 * methods will take care of configuring the connection with the API
 */
object TwitterUtil {

    private val CONSUMER_SECRET = "eGaL0bIj6Y0p84cs6RZdw7WvXoq9EkDF9KES0bPnhw"
    private val CONSUMER_KEY = "AdtZzl6c9v4QiqC6yHWSVw"
    private val ACCESS_TOKEN = "752283427-izyU9iznlS6LHIGjwWaq9RK9mQuaVCr6XpYnJwEB"
    private val ACCESS_TOKEN_SECRET = "qfSQnSs7jQUnNHqt14SWJcY1KmVVm7mf3azPFT4a2OuqV"

    /**
     * @param users an array with all the users to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    @Throws(TwitterException::class)
    fun getTweets(users: Array<String>): ArrayList<PS2Tweet> {
        val twitter = configureTwitter()
        var tweetsFound = ArrayList<PS2Tweet>()
        tweetsFound = retrieveTweets(twitter, users)
        return tweetsFound
    }

    /**
     * @param users the user to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException this exception will ocur when there is a problem contacting
     * the twiter API
     */
    @Throws(TwitterException::class)
    fun getTweets(user: String): ArrayList<PS2Tweet> {
        val twitter = configureTwitter()
        var tweetsFound = ArrayList<PS2Tweet>()
        val twitterUser = arrayOf(user)
        tweetsFound = retrieveTweets(twitter, twitterUser)
        return tweetsFound
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
     * @param users   a list of users to retrieve tweets from
     * @return an arraylist with all the tweets found
     * @throws TwitterException this exception is thrown where there is an error
     * communicating with the twitter API
     */
    @Throws(TwitterException::class)
    private fun retrieveTweets(twitter: Twitter, users: Array<String>): ArrayList<PS2Tweet> {
        val usersFound = twitter.lookupUsers(*users)
        val tweetsFound = ArrayList<PS2Tweet>()
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
                        imgUrl = status3.retweetedStatus.user.biggerProfileImageURL
                        text = status3.text + "\nRetweeted by " + status3.user.screenName
                    } else {
                        name = status3.user.name
                        tag = foundUser.screenName
                        imgUrl = status3.user.biggerProfileImageURL
                        text = status3.text
                    }
                    tweetsFound.add(
                        PS2Tweet(
                            java.lang.Long.toString(status3.id),
                            name,
                            (status3.createdAt.time / 1000).toInt(),
                            text,
                            tag,
                            imgUrl
                        )
                    )
                }
            }
        }
        return tweetsFound
    }
}
