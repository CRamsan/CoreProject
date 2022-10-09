package me.cesar.application.frontend.article

import androidx.compose.runtime.collectAsState
import me.cesar.application.frontend.theme.KotlinLibsTheme
import me.cesar.application.frontend.widgets.Footer
import me.cesar.application.frontend.widgets.Header
import org.jetbrains.compose.web.renderComposable

/**
 * @author cramsan
 */
fun articlePage(articleId: String) {
    val viewModel = ArticleViewModel()
    viewModel.loadPage(articleId)
    renderComposable(rootElementId = "root") {
        val uiState = viewModel.uiState.collectAsState()
        val article = uiState.value.article
        val isLoading = uiState.value.isLoading
        KotlinLibsTheme(
            header = { Header() },
            content = {
                ArticleScreen(
                    article,
                    isLoading,
                )
            },
            footer = { Footer() },
        )
    }
}
