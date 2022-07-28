package com.cramsan.framework.core.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.cramsan.framework.core.BaseFragment
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.userevents.logEvent

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
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.events().observe(
            viewLifecycleOwner,
        ) {
            it?.let { event ->
                onViewModelEvent(event)
            }
        }
        return ComposeView(requireContext()).apply {
            setContent {
                ApplyTheme {
                    CreateComposeContent()
                }
            }
        }
    }

    /**
     * Implement this function to start to compose your UI.
     */
    @Composable
    abstract fun CreateComposeContent()

    /**
     * Set the default theme for this activity.
     */
    @Composable
    abstract fun ApplyTheme(content: @Composable () -> Unit)

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
