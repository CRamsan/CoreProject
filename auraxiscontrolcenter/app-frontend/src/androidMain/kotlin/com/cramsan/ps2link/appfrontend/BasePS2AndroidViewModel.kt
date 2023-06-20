package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.BaseAndroidViewModel

/**
 *
 */
abstract class BasePS2AndroidViewModel<VM : BasePS2ViewModel>(
    override val viewModel: VM,
) : BaseAndroidViewModel(viewModel)
