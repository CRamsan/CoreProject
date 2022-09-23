package me.cesar.application.spring.service

import me.cesar.application.common.model.Article
import me.cesar.application.spring.storage.ddb.DDBArticleRepository
import org.springframework.stereotype.Service

/**
 * Internal service to control how to expose access to the [Article] instances.
 *
 * @author cramsan
 */
@Service
class ArticleService(
    private val repository: DDBArticleRepository,
) {
    /**
     * Fetch a single [Article] with [id]. If no match is found, return null.
     */
    fun findArticle(
        id: String,
    ): Result<Article> {
        return repository.findArticle(id)
    }

    /**
     * Fetch a list of all the articles. This result is not paginated.
     */
    fun findAll(): Result<List<Article>> {
        return repository.findAll()
    }

    /**
     * Insert the [article] into persistent storage.
     */
    fun insert(article: Article): Result<Unit> {
        return repository.insert(article)
    }

    /**
     * Insert the [articles] list into storage.
     */
    fun insert(articles: List<Article>): Result<Unit> {
        return repository.insert(articles)
    }
}
