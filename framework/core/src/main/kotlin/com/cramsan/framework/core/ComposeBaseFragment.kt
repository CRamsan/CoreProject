package com.cramsan.framework.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

/**
 * This class extends [BaseFragment] with the capabilities to render a Compose screen. The [viewModel]
 * is a required field. If this class does not need a viewModel, then [NoopViewModel] can be used.
 */
abstract class ComposeBaseFragment<VM : BaseViewModel> : BaseFragment() {

    /**
     * ViewModel instance that will be used to manage this [Fragment]. The class that extends [BaseFragment] is in
     * charge of providing the implementation.
     */
    protected abstract val viewModel: VM

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        viewModel.events().observe(
            viewLifecycleOwner,
        ) {
            it?.let {
                onViewModelEvent(it)
            }
        }
        return view
    }
}
