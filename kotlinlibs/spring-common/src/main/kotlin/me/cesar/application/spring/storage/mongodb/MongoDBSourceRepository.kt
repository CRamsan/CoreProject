package me.cesar.application.spring.storage.mongodb

import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.storage.mongodb.entity.SourceEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository class for querying within the Mongo DB for accounts
 */
@Repository
interface MongoDBSourceRepository : MongoRepository<SourceEntity, String> {

    /**
     * Return a list of [SourceEntity] that match for the [sourceType].
     */
    fun findBySourceType(sourceType: SourceType, pageable: Pageable): Page<SourceEntity>
}
