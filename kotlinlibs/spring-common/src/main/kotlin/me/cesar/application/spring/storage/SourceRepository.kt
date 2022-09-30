package me.cesar.application.spring.storage

import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Interface for a repository that can save entities of type [Source].
 *
 * @author cramsan
 */
interface SourceRepository {

    /**
     * Return a [Source] identified by [id] and [sourceType].
     */
    fun findSource(id: String, sourceType: SourceType): Result<Source?>

    /**
     * Persist the [source] into storage.
     */
    fun insert(source: Source): Result<Source>

    /**
     * Return a list of all [Source] of type [sourceType]. The result is not paginated.
     */
    fun findAll(sourceType: SourceType, pageable: Pageable?): Result<Page<Source>>

    /**
     * Return a list of all [Source]. The result is not paginated.
     */
    fun findAll(pageable: Pageable?): Result<Page<Source>>

    /**
     * Save the changes to the [source].
     */
    fun save(source: Source): Result<Unit>
}
