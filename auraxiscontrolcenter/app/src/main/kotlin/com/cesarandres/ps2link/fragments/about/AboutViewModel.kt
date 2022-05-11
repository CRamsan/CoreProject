package com.cesarandres.ps2link.fragments.about

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenUrl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val resources: Resources,
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
    application,
    pS2LinkRepository,
    pS2Settings,
    dispatcherProvider,
    savedStateHandle,
),
    AboutEventHandler {

    override val logTag: String
        get() = "AboutViewModel"

    override fun onAboutClick() {
        events.value = OpenUrl(resources.getString(R.string.url_homepage))
    }
}
