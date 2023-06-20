package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.BaseViewModel

/**
 *
 */
class NoopPS2AndroidViewModel(
    viewModel: NoopPS2ViewModel,
) : BasePS2AndroidViewModel<NoopPS2ViewModel>(
    viewModel,
),
    BaseViewModel by viewModel
