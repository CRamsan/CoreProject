package me.cesar.application.spring.service

import me.cesar.application.common.model.Article
import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.storage.ddb.DDBSourceRepository
import org.springframework.stereotype.Service

/**
 * Internal service to control how to expose access to the [Source] instances.
 *
 * @author cramsan
 */
@Service
class SourceService(
    private val repository: DDBSourceRepository,
) {
    /**
     * Fetch a single [Article] with [id]. If no match is found, return null.
     */
    fun findSource(
        id: String,
        sourceType: SourceType,
    ): Result<Source> {
        return repository.findSource(id, sourceType)
    }

    /**
     * Insert the [source] into storage.
     */
    fun insert(source: Source): Result<Source> {
        return repository.insert(source)
    }

    /**
     * Return a list of all sources. The result is not paginated.
     */
    fun findAll(): Result<List<Source>> {
        return repository.findAll()
    }
}
