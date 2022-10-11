package me.cesar.application.frontend.article

import me.cesar.application.common.model.Article

/**
 * @author cramsan
 */
data class ArticleUIModel(
    val article: Article?,
    val isLoading: Boolean,
)
