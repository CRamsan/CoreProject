package me.cesar.application.spring.service

import me.cesar.application.common.model.Article
import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.storage.SourceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Internal service to control how to expose access to the [Source] instances.
 *
 * @author cramsan
 */
@Service
class SourceService(
    private val repository: SourceRepository,
) {
    /**
     * Fetch a single [Article] with [id]. If no match is found, return null.
     */
    fun findSource(
        id: String,
    ): Result<Source?> {
        return repository.findSource(id)
    }

    /**
     * Insert the [source] into storage.
     */
    fun insert(source: Source): Result<Source> {
        return repository.insert(source)
    }

    /**
     * Return a list of all sources. The result is paginated.
     */
    fun findAll(pageable: Pageable?): Result<Page<Source>> {
        return repository.findAll(pageable)
    }

    /**
     * Return a list of all [Source] of type [sourceType]. The result is paginated.
     */
    fun findAll(sourceType: SourceType, pageable: Pageable?): Result<Page<Source>> {
        return repository.findAll(sourceType, pageable)
    }

    /**
     * Save the changes to the [source].
     */
    fun save(source: Source): Result<Unit> {
        return repository.save(source)
    }
}
