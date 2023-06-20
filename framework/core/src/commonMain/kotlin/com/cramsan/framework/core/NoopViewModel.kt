package com.cramsan.framework.core

/**
 * Simple viewModel that is used a placeholder when there is no need for a viewModel, but one is required.
 */
class NoopViewModel constructor(
    dispatcherProvider: DispatcherProvider,
) : BaseViewModelImpl<BaseEvent>(dispatcherProvider) {
    override val logTag: String
        get() = "NoopViewModel"
}
