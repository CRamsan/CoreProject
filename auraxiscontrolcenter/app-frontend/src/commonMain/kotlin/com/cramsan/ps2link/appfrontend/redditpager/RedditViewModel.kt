package com.cramsan.ps2link.appfrontend.redditpager

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.RedditRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.RedditPage
import com.cramsan.ps2link.core.models.RedditPost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 */
class RedditViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    languageProvider: LanguageProvider,
    private val redditRepository: RedditRepository,
    dispatcherProvider: DispatcherProvider,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    RedditEventHandler,
    RedditViewModelInterface {

    override val logTag: String
        get() = "RedditViewModel"

    // State
    private val _redditContent = MutableStateFlow<ImmutableList<RedditPostUIModel>>(persistentListOf())
    override val redditContent = _redditContent.asStateFlow()

    private lateinit var redditPage: RedditPage

    override fun setUp(redditPage: RedditPage) {
        this.redditPage = redditPage
        onRefreshRequested()
    }

    override fun onPostSelected(redditPost: RedditPostUIModel) {
        redditPost.postUrl?.let {
            viewModelScope.launch {
                _events.emit(BasePS2Event.OpenUrl(it))
            }
        }
    }

    override fun onImageSelected(redditPost: RedditPostUIModel) {
        redditPost.url?.let {
            viewModelScope.launch {
                _events.emit(BasePS2Event.OpenUrl(it))
            }
        }
    }

    override fun onRefreshRequested() {
        loadingStarted()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val response = redditRepository.getPosts(redditPage)
            if (response.isSuccessful) {
                _redditContent.value = response.requireBody().toUIModel().toImmutableList()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}

private fun List<RedditPost>.toUIModel(): List<RedditPostUIModel> {
    return map { it.toUIModel() }
}

private fun RedditPost.toUIModel(): RedditPostUIModel {
    return RedditPostUIModel(
        url = url,
        title = title,
        imgUr = imgUr,
        author = author,
        label = label,
        upvotes = upvotes,
        comments = comments,
        createdTime = createdTime.toString(),
        postUrl = postUrl,
    )
}

/**
 *
 */
interface RedditViewModelInterface : BasePS2ViewModelInterface {
    val redditContent: StateFlow<ImmutableList<RedditPostUIModel>>
    /**
     *
     */
    fun setUp(redditPage: RedditPage)
}
