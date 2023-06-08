package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository

class NoopPS2AndroidViewModel(
    viewModel: NoopPS2ViewModel,
) : BasePS2AndroidViewModel<NoopPS2ViewModel>(
    viewModel,
) {
    override val logTag: String
        get() = "NoopPS2AndroidViewModel"
}