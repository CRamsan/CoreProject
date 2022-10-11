package me.cesar.application.frontend.home

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.cesar.application.frontend.APIClient

/**
 * @author cramsan
 */
class HomeViewModel(
    private val client: APIClient,
) {
    private val scope = MainScope()

    private val _uiState = MutableStateFlow(
        HomeUIModel(
            articles = emptyList(),
            showButtonNext = false,
            showButtonPrevious = false,
            isLoading = true,
        ),
    )
    val uiState = _uiState.asStateFlow()

    /**
     * Trigger the load of data for this page.
     */
    fun loadPage() {
        scope.launch {
            val data = client.getArticles("")
            _uiState.value = HomeUIModel(
                articles = data.content,
                showButtonNext = true,
                showButtonPrevious = true,
                isLoading = false,
            )
        }
    }

    /**
     * On Article link pressed.
     */
    fun onArticleSelected() {
        TODO()
    }

    /**
     * On button for next page pressed
     */
    fun onButtonNextSelected() {
        TODO()
    }

    /**
     * On button for previous page pressed
     */
    fun onButtonPreviousSelected() {
        TODO()
    }
}
