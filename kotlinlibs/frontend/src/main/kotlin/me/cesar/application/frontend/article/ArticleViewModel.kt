package me.cesar.application.frontend.article

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.cesar.application.frontend.APIClient

/**
 * @author cramsan
 */
class ArticleViewModel(
    private val client: APIClient,
) {
    private val scope = MainScope()

    private val _uiState = MutableStateFlow(
        ArticleUIModel(null, true),
    )
    val uiState = _uiState.asStateFlow()

    /**
     * Load a page for the article with the given [articleId].
     */
    fun loadPage(articleId: String) {
        scope.launch {
            _uiState.value = ArticleUIModel(
                article = client.getArticle(articleId),
                isLoading = false,
            )
        }
    }
}
