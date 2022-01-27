package com.cramsan.ps2link.appcore.repository

import com.cramsan.framework.core.BackgroundModuleLifecycleAwareComponent
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.LifecycleAwareComponent
import com.cramsan.framework.remoteconfig.RemoteConfig
import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.cramsan.ps2link.core.models.PS2Tweet
import com.cramsan.ps2link.remoteconfig.RemoteConfigData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TwitterRepositoryImpl(
    private val twitterClient: TwitterClient,
    private val remoteConfig: RemoteConfig<RemoteConfigData>,
    private val dispatcherProvider: DispatcherProvider,
) : TwitterRepository,
    LifecycleAwareComponent by BackgroundModuleLifecycleAwareComponent(dispatcherProvider) {

    private val followedUsers = MutableStateFlow<Map<String, Boolean>>(mapOf())

    private val tweetList = MutableStateFlow<PS2HttpResponse<List<PS2Tweet>>?>(null)

    private val dataMutex = Mutex()

    init {
        followedUsers.onEach {
            val userList = it.toUserList()
            getTweetsForUsers(userList)
        }.launchIn(scope)
    }

    private fun loadUsers() {
        if (followedUsers.value.isNotEmpty()) {
            return
        }

        val twitterUsers = remoteConfig.getConfigPayloadOrDefault().twitterUsernames
        val availableUsers = twitterUsers.associateWith { true }.toMutableMap()

        followedUsers.value = availableUsers
    }

    override suspend fun getTwitterUsers(): Map<String, Boolean> {
        loadUsers()
        return followedUsers.value
    }

    override suspend fun setFollowStatus(user: String, follow: Boolean) {
        followedUsers.value = followedUsers.value.toMutableMap().apply {
            put(user, follow)
        }
    }

    override suspend fun getTweets(): PS2HttpResponse<List<PS2Tweet>> {
        loadUsers()
        val userList = followedUsers.value.toUserList()
        return getTweetsForUsers(userList)
    }

    override fun getTweetsAsFlow(): StateFlow<PS2HttpResponse<List<PS2Tweet>>?> = tweetList

    override fun getTwitterUsersAsFlow(): StateFlow<Map<String, Boolean>> = followedUsers

    private suspend fun getTweetsForUsers(users: List<String>): PS2HttpResponse<List<PS2Tweet>> = dataMutex.withLock {
        val tweets = twitterClient.getTweets(users)
        tweetList.value = tweets
        return tweets
    }

    private fun Map<String, Boolean>.toUserList(): List<String> {
        return filter { it.value }.keys.toList()
    }
}
