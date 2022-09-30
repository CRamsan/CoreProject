package me.cesar.application.spring.storage.mongodb

import me.cesar.application.common.model.Article
import me.cesar.application.spring.storage.ArticleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

/**
 * CRUD repository for [Article] entities. This implementation allows for pagination.
 *
 * @author cramsan
 */
@Repository
class MongoDBArticleRepositoryProxy(
    private val mongoDBArticleRepository: MongoDBArticleRepository,
) : ArticleRepository {

    override fun findArticle(id: String): Result<Article?> = runCatching {
        val result = mongoDBArticleRepository.findById(id)
        if (result.isPresent) {
            result.get()
        } else {
            null
        }?.toModel()
    }

    override fun insert(article: Article): Result<Unit> = runCatching {
        val entity = article.toStorage()
        mongoDBArticleRepository.insert(entity)
    }

    override fun insert(articles: List<Article>): Result<Unit> = runCatching {
        val toBeInserted = articles.map { it.toStorage() }
        mongoDBArticleRepository.insert(toBeInserted)
    }

    override fun findAll(pageable: Pageable?): Result<Page<Article>> = runCatching {
        mongoDBArticleRepository.findAll(pageable ?: firstPage).map { it.toModel() }
    }

    override fun findAll(sourceId: String, pageable: Pageable?): Result<Page<Article>> = runCatching {
        mongoDBArticleRepository.findBySourceId(sourceId, pageable ?: firstPage).map { it.toModel() }
    }

    override fun save(article: Article): Result<Unit> = runCatching {
        mongoDBArticleRepository.save(article.toStorage())
    }

    companion object {
        val firstPage: Pageable = PageRequest.of(1, 10)
    }
}
