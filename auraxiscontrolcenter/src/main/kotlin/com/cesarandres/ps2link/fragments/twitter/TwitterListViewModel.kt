package com.cesarandres.ps2link.fragments.twitter

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.TwitterRepository
import com.cramsan.ps2link.appcore.twitter.TwitterUser
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TwitterListViewModel @Inject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    private val twitterRepository: TwitterRepository,
    savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
        application,
        pS2LinkRepository,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    TweetListComposeEventHandler {

    override val logTag: String
        get() = "TwitterListViewModel"

    // State
    val tweetList = twitterRepository.getTweetsAsFlow().map { list ->
        list.sortedByDescending { twit ->
            twit.date
        }
    }.asLiveData()
    val twitterUsers = twitterRepository.getTwitterUsersAsFlow().asLiveData()

    override fun onTwitterUserClicked(twitterUser: TwitterUser) {
        ioScope.launch {
            loadingStarted()
            val following = twitterRepository.getTwitterUsers()[twitterUser] ?: true
            twitterRepository.setFollowStatus(twitterUser, !following)
            loadingCompleted()
        }
    }
}
