package me.cesar.application.frontend.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant
import me.cesar.application.common.model.Article

/**
 * @author cramsan
 */
class HomeViewModel {

    private val _uiState = MutableStateFlow(
        HomeUIModel(
            articles = (0..12).map {
                Article(
                    id = "$it",
                    title = "Title $it",
                    sourceId = "324423234324",
                    content = "This is some text",
                    bannerUrl = "https://blog.jetbrains.com/wp-content/" +
                        "uploads/2022/09/Featured_image_1280x600_EduTools.png",
                    publishedTime = Instant.fromEpochSeconds(100),
                )
            },
            showButtonNext = true,
            showButtonPrevious = true,
            isLoading = true,
        ),
    )
    val uiState = _uiState.asStateFlow()

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
