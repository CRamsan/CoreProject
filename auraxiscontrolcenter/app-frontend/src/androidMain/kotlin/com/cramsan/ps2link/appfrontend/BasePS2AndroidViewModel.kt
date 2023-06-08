package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.BaseAndroidViewModel
import com.cramsan.framework.core.BaseViewModelImpl
import com.cramsan.framework.core.DispatcherProvider

abstract class BasePS2AndroidViewModel<VM : BasePS2ViewModel>(
    viewModel: VM,
) : BaseAndroidViewModel(viewModel), BasePS2ViewModelInterface by viewModel