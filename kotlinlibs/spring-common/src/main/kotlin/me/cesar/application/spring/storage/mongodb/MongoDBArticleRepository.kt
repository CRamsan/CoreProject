package me.cesar.application.spring.storage.mongodb

import me.cesar.application.spring.storage.mongodb.entity.ArticleEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository class for querying within the Mongo DB for accounts
 */
@Repository
interface MongoDBArticleRepository : MongoRepository<ArticleEntity, String> {

    /**
     * Find a list of [ArticleEntity] that match the [sourceId] and for the requested [pageable]. The result is
     * paginated.
     */
    fun findBySourceId(sourceId: String, pageable: Pageable): Page<ArticleEntity>
}
