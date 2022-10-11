package me.cesar.application.frontend.home

import androidx.compose.runtime.collectAsState
import me.cesar.application.frontend.APIClient
import me.cesar.application.frontend.theme.KotlinLibsTheme
import me.cesar.application.frontend.widgets.Footer
import me.cesar.application.frontend.widgets.Header
import org.jetbrains.compose.web.renderComposable

/**
 * @author cramsan
 */
fun homePage(client: APIClient) {
    val viewModel = HomeViewModel(client)
    viewModel.loadPage()
    renderComposable(rootElementId = "root") {
        val uiState = viewModel.uiState.collectAsState()
        val articles = uiState.value.articles
        val showButtonNext = uiState.value.showButtonNext
        val showButtonPrevious = uiState.value.showButtonPrevious
        val isLoading = uiState.value.isLoading
        KotlinLibsTheme(
            header = { Header() },
            content = {
                HomeScreen(
                    articles,
                    showButtonNext,
                    showButtonPrevious,
                    isLoading,
                )
            },
            footer = { Footer() },
        )
    }
}
