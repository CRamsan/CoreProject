package com.cramsan.ps2link.network.ws.testgui.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Base class for all screens. It takes care of managing the ViewModel and to provide an entry point to render the
 * screen.
 */
abstract class BaseScreen<VM : BaseViewModel> {

    protected abstract fun createViewModel(): VM

    /**
     * Compose this screen based on the value of [isVisible]. This function will also manage the ViewModel lifecycle.
     */
    @Composable
    fun ComposeScreen(isVisible: Boolean) {
        val viewModel by remember { mutableStateOf(createViewModel()) }

        if (isVisible) {
            viewModel.onStart()
        } else {
            viewModel.onClose()
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally(),
        ) {
            ScreenContent(viewModel)
        }
    }

    /**
     * Compose the content of the screen for how it should be rendered when visible.
     */
    @Composable
    protected abstract fun ScreenContent(viewModel: VM)
}
