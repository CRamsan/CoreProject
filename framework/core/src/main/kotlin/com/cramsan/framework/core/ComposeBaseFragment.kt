package com.cramsan.framework.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper

/**
 * This class extends [BaseFragment] with the capabilities to render a Compose screen. The [viewModel]
 * is a required field. If this class does not need a viewModel, then [NoopViewModel] can be used.
 */
abstract class ComposeBaseFragment<VM : BaseViewModel> : BaseFragment() {

    abstract val viewModel: VM

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.events().observe(
            viewLifecycleOwner,
            {
                it?.let {
                    onViewModelEvent(it)
                }
            }
        )
        return null
    }
}
