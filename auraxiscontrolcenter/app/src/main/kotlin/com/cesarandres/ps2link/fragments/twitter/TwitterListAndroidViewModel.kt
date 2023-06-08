package com.cesarandres.ps2link.fragments.twitter

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.twitter.TweetListComposeEventHandler
import com.cramsan.ps2link.appfrontend.twitter.TwitterListViewModel
import com.cramsan.ps2link.appfrontend.twitter.TwitterListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class TwitterListAndroidViewModel @Inject constructor(
    viewModel: TwitterListViewModel,
) : BasePS2AndroidViewModel<TwitterListViewModel>(
    viewModel,
),
    TweetListComposeEventHandler by viewModel,
    TwitterListViewModelInterface by viewModel {

    override val logTag: String
        get() = "TwitterListViewModel"
}
