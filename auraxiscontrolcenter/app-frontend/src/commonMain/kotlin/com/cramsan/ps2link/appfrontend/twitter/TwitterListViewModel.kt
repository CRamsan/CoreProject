package com.cramsan.ps2link.appfrontend.twitter

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logW
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.TwitterRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.PS2Tweet
import com.cramsan.ps2link.ui.items.PS2TweetUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 *
 */
interface TwitterListViewModelInterface : BasePS2ViewModelInterface {
    // State
    val tweetList: Flow<ImmutableList<PS2TweetUIModel>>
    val twitterUsers: StateFlow<Map<String, Boolean>>
    /**
     *
     */
    fun setUp()
}

/**
 *
 */
class TwitterListViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    languageProvider: LanguageProvider,
    dispatcherProvider: DispatcherProvider,
    private val twitterRepository: TwitterRepository,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    TweetListComposeEventHandler,
    TwitterListViewModelInterface {

    override val logTag: String
        get() = "TwitterListViewModel"

    // State
    override val tweetList = twitterRepository.getTweetsAsFlow().map { response ->
        if (response == null) {
            return@map persistentListOf()
        }
        if (response.isSuccessful) {
            loadingCompleted()
            val uiModels = response.requireBody().sortedByDescending { twit ->
                twit.date
            }.map {
                it.toUIModel()
            }.toImmutableList()
            uiModels
        } else {
            loadingCompletedWithError()
            persistentListOf()
        }
    }
    override val twitterUsers = twitterRepository.getTwitterUsersAsFlow()

    override fun setUp() {
        onRefreshRequested()
    }

    override fun onTwitterUserClicked(twitterUser: String) {
        loadingStarted()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val following = twitterRepository.getTwitterUsers()[twitterUser] ?: true
            twitterRepository.setFollowStatus(twitterUser, !following)
        }
    }

    override fun onRefreshRequested() {
        loadingStarted()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            // This will trigger a new fetch for new tweets. The actual tweets will still be loaded
            // through [getTweetsAsFlow].
            twitterRepository.getTweets()
        }
    }

    override fun onTweetSelected(tweet: PS2TweetUIModel) {
        if (tweet.sourceUrl.isBlank()) {
            logW(logTag, "Source Url is empty and cannot be opened")
            return
        }
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenUrl(tweet.sourceUrl))
        }
    }
}

private fun PS2Tweet.toUIModel(): PS2TweetUIModel {
    return PS2TweetUIModel(
        user = user,
        content = content,
        tag = tag,
        creationTime = date.toString(),
        imgUrl = imgUrl,
        id = id,
        sourceUrl = sourceUrl,
    )
}
