package com.cramsan.framework.core

import androidx.lifecycle.ViewModel

/**
 * The ViewModel holds the business logic that powers a screen for an Android application. This class
 * provides some helpful defaults. There is a [dispatcherProvider] that provides access to different
 * types of dispatchers, this is particularly useful for dispatching work to a background thread
 * without having to freeze the UI thread. [savedStateHandle] can be used to persist the state of the
 * viewModel in case the system needs to kill/restart the process. ViewModels already keep their state
 * on configuration change so [savedStateHandle] should not be used in those cases.
 */
abstract class BaseAndroidViewModel(
    protected val viewModel: BaseViewModel,
) : BaseViewModel by viewModel, ViewModel()
