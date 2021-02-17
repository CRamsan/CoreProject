package com.cramsan.ps2link.appcore.repository

import com.cramsan.framework.core.BackgroundModuleLifecycleAwareComponent
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.LifecycleAwareComponent
import com.cramsan.framework.preferences.Preferences
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.cramsan.ps2link.appcore.twitter.TwitterUser
import com.cramsan.ps2link.core.models.PS2Tweet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TwitterRepositoryImpl(
    private val twitterClient: TwitterClient,
    private val preferences: Preferences,
    private val dispatcherProvider: DispatcherProvider,
) : TwitterRepository,
    LifecycleAwareComponent by BackgroundModuleLifecycleAwareComponent(dispatcherProvider) {

    private val followedUsers = MutableStateFlow<MutableMap<TwitterUser, Boolean>>(mutableMapOf())
    private val tweetList = MutableStateFlow<List<PS2Tweet>>(emptyList())

    private val dataMutex = Mutex()

    init {
        scope.launch {
            initialize()
        }
    }

    private suspend fun initialize() {
        val availableUsers = TwitterUser.values().associate { it to false }.toMutableMap()
        val savedUsers = preferences.loadString(SUBSCRIBED_USER_LIST_PREF)
        if (savedUsers == null) {
            // This would be the case the first time the user tried to access twitter.
            // Therefore we will enable all users.
            val subscribedUserList = TwitterUser.values().map { it.handle }.toStringList()
            preferences.saveString(SUBSCRIBED_USER_LIST_PREF, subscribedUserList)
            TwitterUser.values().forEach {
                availableUsers[it] = true
            }
        } else {
            val subscribedUsers = savedUsers.toList().mapNotNull { TwitterUser.fromString(it) }
            subscribedUsers.forEach {
                availableUsers[it] = true
            }
        }

        followedUsers.value = availableUsers

        followedUsers.onEach {
            val userList = it.toUserList()
            getTweetsForUsers(userList)
        }.launchIn(scope)
    }

    override suspend fun getTwitterUsers(): Map<TwitterUser, Boolean> {
        return followedUsers.value
    }

    override suspend fun setFollowStatus(user: TwitterUser, follow: Boolean) {
        followedUsers.value.let {
            it[user] = follow
        }
    }

    override suspend fun getTweets(): List<PS2Tweet> {
        val userList = followedUsers.value.toUserList()
        getTweetsForUsers(userList)
        return tweetList.value
    }

    override fun getTweetsAsFlow(): StateFlow<List<PS2Tweet>> = tweetList

    private suspend fun getTweetsForUsers(users: List<TwitterUser>): List<PS2Tweet> = dataMutex.withLock {
        val tweets = twitterClient.getTweets(users)
        tweetList.value = tweets
        return tweets
    }

    private fun MutableMap<TwitterUser, Boolean>.toUserList(): List<TwitterUser> {
        return filter { it.value }.keys.toList()
    }

    private fun List<String>.toStringList(): String {
        return this.joinToString(",")
    }

    private fun String.toList(): List<String> {
        return this.split(",")
    }

    companion object {
        const val SUBSCRIBED_USER_LIST_PREF = "SUBSCRIBED_USER_LIST_PREF"
    }
}
