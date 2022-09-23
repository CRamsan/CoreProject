package me.cesar.application.spring.storage

import me.cesar.application.common.model.Article

/**
 * Interface for a repository that can save entities of type [Article].
 *
 * @author cramsan
 */
interface ArticleRepository {

    /**
     * Return a single [Article] identified by an [id].
     */
    fun findArticle(id: String): Result<Article>

    /**
     * Persist [article] into storage.
     */
    fun insert(article: Article): Result<Unit>

    /**
     * Persist the [articles] list into storage.
     */
    fun insert(articles: List<Article>): Result<Unit>

    /**
     * Return a list of all [Article]. This return value is not paginated.
     */
    fun findAll(): Result<List<Article>>

    /**
     * Return a list of all [Article] from the source identified by [sourceId]. This return value is not paginated.
     */
    fun findAll(sourceId: String): Result<List<Article>>
}
