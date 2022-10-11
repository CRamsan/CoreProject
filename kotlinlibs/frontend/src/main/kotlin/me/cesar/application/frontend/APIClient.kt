package me.cesar.application.frontend

import me.cesar.application.common.model.Article
import me.cesar.application.common.network.PageResponse

/**
 * @author cramsan
 *
 * Interface to be able to fetch data.
 */
interface APIClient {

    /**
     * Get an [Article] based on the [articleId].
     */
    suspend fun getArticle(articleId: String): Article

    /**
     * Get a paginated list of [Article] for the [sourceId].
     */
    suspend fun getArticles(sourceId: String): PageResponse<Article>
}
