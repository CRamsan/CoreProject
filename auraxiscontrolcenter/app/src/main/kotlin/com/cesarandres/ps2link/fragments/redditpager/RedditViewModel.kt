package com.cesarandres.ps2link.fragments.redditpager

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenUrl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.RedditRepository
import com.cramsan.ps2link.core.models.RedditPage
import com.cramsan.ps2link.core.models.RedditPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditViewModel @Inject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    private val redditRepository: RedditRepository,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
    application,
    pS2LinkRepository,
    pS2Settings,
    dispatcherProvider,
    savedStateHandle,
),
    RedditEventHandler {

    override val logTag: String
        get() = "RedditViewModel"

    // State
    private val _redditContent = MutableStateFlow<List<RedditPost>>(emptyList())
    val redditContent = _redditContent.asStateFlow()

    private lateinit var redditPage: RedditPage

    fun setUp(redditPage: RedditPage) {
        this.redditPage = redditPage
        onRefreshRequested()
    }

    override fun onPostSelected(redditPost: RedditPost) {
        redditPost.postUrl?.let {
            events.value = OpenUrl(it)
        }
    }

    override fun onImageSelected(redditPost: RedditPost) {
        redditPost.url?.let {
            events.value = OpenUrl(it)
        }
    }

    override fun onRefreshRequested() {
        loadingStarted()
        ioScope.launch {
            val response = redditRepository.getPosts(redditPage)
            if (response.isSuccessful) {
                _redditContent.value = response.requireBody()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}
