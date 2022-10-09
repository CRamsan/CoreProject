package me.cesar.application.frontend.article

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant
import me.cesar.application.common.model.Article

/**
 * @author cramsan
 */
class ArticleViewModel {

    private val _uiState = MutableStateFlow(
        ArticleUIModel(null, true),
    )
    val uiState = _uiState.asStateFlow()

    /**
     * Load a page for the article with the given [articleId].
     */
    fun loadPage(articleId: String) {
        _uiState.value = ArticleUIModel(
            article = Article(
                id = articleId,
                title = "Title",
                sourceId = "324423234324",
                content = "This is some text",
                bannerUrl = "https://blog.jetbrains.com/wp-content/uploads/" +
                    "2022/09/Featured_image_1280x600_EduTools.png",
                publishedTime = Instant.fromEpochSeconds(100),
            ),
            isLoading = false,
        )
    }
}
