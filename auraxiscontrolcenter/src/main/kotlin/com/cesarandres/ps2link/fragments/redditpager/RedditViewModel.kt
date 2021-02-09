package com.cesarandres.ps2link.fragments.redditpager

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.RedditRepository
import com.cramsan.ps2link.core.models.RedditPage
import com.cramsan.ps2link.core.models.RedditPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RedditViewModel @ViewModelInject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    private val redditRepository: RedditRepository,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
        application,
        pS2LinkRepository,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    RedditEventHandler {

    override val logTag: String
        get() = "RedditViewModel"

    // State
    private val _redditContent = MutableStateFlow<List<RedditPost>>(emptyList())
    val redditContent = _redditContent.asStateFlow()

    fun setUp(redditPage: RedditPage) {
        ioScope.launch {
            _redditContent.value = redditRepository.getPosts(redditPage)
        }
    }

    override fun onPostSelected(redditPost: RedditPost) {
        // events.value = OpenProfile(profileId, namespace)
    }
}
