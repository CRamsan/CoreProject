package com.cesarandres.ps2link.fragments.about

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.about.AboutEventHandler
import com.cramsan.ps2link.appfrontend.about.AboutViewModel
import com.cramsan.ps2link.appfrontend.about.AboutViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class AboutAndroidViewModel @Inject constructor(
    viewModel: AboutViewModel,
) : BasePS2AndroidViewModel<AboutViewModel>(
    viewModel,
),
    AboutViewModelInterface by viewModel,
    AboutEventHandler by viewModel
