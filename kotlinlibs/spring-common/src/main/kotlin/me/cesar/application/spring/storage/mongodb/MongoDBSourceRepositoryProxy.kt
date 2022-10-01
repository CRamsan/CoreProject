package me.cesar.application.spring.storage.mongodb

import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.storage.SourceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

/**
 * CRUD repository for [SourceEntity] entities. This implementation allows for pagination.
 *
 * @author cramsan
 */
@Repository
class MongoDBSourceRepositoryProxy(
    private val mongoDBSourceRepository: MongoDBSourceRepository,
) : SourceRepository {

    override fun findSource(
        id: String,
    ): Result<Source?> = runCatching {
        val result = mongoDBSourceRepository.findById(id)
        val entity = if (result.isPresent) {
            result.get()
        } else {
            null
        }
        entity?.toModel()
    }

    override fun insert(source: Source) = runCatching {
        val entity = source.toStorage()
        mongoDBSourceRepository.insert(entity)
        entity.toModel()
    }

    override fun findAll(sourceType: SourceType, pageable: Pageable?): Result<Page<Source>> = runCatching {
        mongoDBSourceRepository.findBySourceType(sourceType, pageable ?: firstPage).map { it.toModel() }
    }

    override fun findAll(pageable: Pageable?): Result<Page<Source>> = runCatching {
        if (pageable == null) {
            val entities = mongoDBSourceRepository.findAll().map { it.toModel() }
            PageImpl(entities)
        } else {
            mongoDBSourceRepository.findAll(pageable).map { it.toModel() }
        }
    }

    override fun save(source: Source): Result<Unit> = runCatching {
        mongoDBSourceRepository.save(source.toStorage())
    }

    companion object {
        val firstPage: Pageable = PageRequest.of(1, 10)
    }
}
