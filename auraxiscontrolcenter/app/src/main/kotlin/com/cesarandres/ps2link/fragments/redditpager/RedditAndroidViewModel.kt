package com.cesarandres.ps2link.fragments.redditpager

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.redditpager.RedditEventHandler
import com.cramsan.ps2link.appfrontend.redditpager.RedditViewModel
import com.cramsan.ps2link.appfrontend.redditpager.RedditViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class RedditAndroidViewModel @Inject constructor(
    viewModel: RedditViewModel,
) : BasePS2AndroidViewModel<RedditViewModel>(
    viewModel,
),
    RedditEventHandler by viewModel,
    RedditViewModelInterface by viewModel
