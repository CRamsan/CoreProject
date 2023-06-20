package com.cramsan.ps2link.appfrontend.about

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.StringProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import kotlinx.coroutines.launch

/**
 *
 */
class AboutViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    languageProvider: LanguageProvider,
    private val stringProvider: StringProvider,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    AboutEventHandler,
    AboutViewModelInterface {

    override val logTag: String
        get() = "AboutViewModel"

    override fun onAboutClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenUrl(stringProvider.urlHomePage()))
        }
    }
}

/**
 *
 */
expect fun StringProvider.urlHomePage(): String

/**
 *
 */
interface AboutViewModelInterface : BasePS2ViewModelInterface
