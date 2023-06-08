package com.cramsan.framework.core.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import com.cramsan.framework.core.BaseAndroidViewModel
import com.cramsan.framework.core.BaseFragment
import com.cramsan.framework.core.BaseViewModelFragment
import kotlinx.coroutines.launch

/**
 * This class extends [BaseFragment] with the capabilities to render a Compose screen. The [viewModel]
 * is a required field. If this class does not need a viewModel, then [NoopViewModel] can be used.
 */
abstract class ComposeBaseFragment<VM : BaseAndroidViewModel> : BaseViewModelFragment<VM>() {

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Start a coroutine in the lifecycle scope
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect {
                onViewModelEvent(it)
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
}
