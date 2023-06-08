package com.cramsan.framework.core

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.cramsan.framework.userevents.logEvent

/**
 * This class extends [BaseFragment] with the capabilities hold a ViewModel. The [viewModel]
 * is a required field. If this class does not need a viewModel, then [NoopViewModel] can be used.
 */
abstract class BaseViewModelFragment<VM : BaseAndroidViewModel> : BaseFragment() {

    /**
     * ViewModel instance that will be used to manage this [Fragment]. The class that extends [BaseFragment] is in
     * charge of providing the implementation.
     */
    protected abstract val viewModel: VM

    @CallSuper
    override fun onStart() {
        super.onStart()
        logEvent(logTag, EVENT_CREATED)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        logEvent(logTag, EVENT_DISPLAYED)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        logEvent(logTag, EVENT_HIDDEN)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        logEvent(logTag, EVENT_DESTROYED)
    }

    companion object {
        private const val EVENT_CREATED = "Created"
        private const val EVENT_DISPLAYED = "Displayed"
        private const val EVENT_HIDDEN = "Hidden"
        private const val EVENT_DESTROYED = "Destroyed"
    }
}
