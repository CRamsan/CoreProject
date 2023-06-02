package com.cramsan.framework.sample.android.app.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cramsan.framework.sample.android.app.UIEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 *
 */
@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private val viewModel: HomePageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    handleEvents(it)
                }
            }
        }
    }

    private fun handleEvents(event: UIEvent) {
        when (event) {
            is UIEvent.NextPage -> navigateNextPage()
            is UIEvent.Noop -> Unit
        }
    }

    private fun navigateNextPage() {
        val navController = findNavController()
        navController.navigate(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState by viewModel.uiState.collectAsState()

                HomePageScreen(
                    uiState.loading,
                    uiState.title,
                    uiState.subtitle,
                    uiState.message,
                    { viewModel.loadData(forceRefresh = true) },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
}
