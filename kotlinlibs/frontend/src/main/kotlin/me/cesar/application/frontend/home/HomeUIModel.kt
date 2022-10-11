package me.cesar.application.frontend.home

import me.cesar.application.common.model.Article

/**
 * @author cramsan
 */
data class HomeUIModel(
    val articles: List<Article>,
    val showButtonNext: Boolean,
    val showButtonPrevious: Boolean,
    val isLoading: Boolean,
)
