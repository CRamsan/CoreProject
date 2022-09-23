package me.cesar.application.spring.storage

import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType

/**
 * Interface for a repository that can save entities of type [Source].
 *
 * @author cramsan
 */
interface SourceRepository {

    /**
     * Return a [Source] identified by [id] and [sourceType].
     */
    fun findSource(id: String, sourceType: SourceType): Result<Source>

    /**
     * Persist the [source] into storage.
     */
    fun insert(source: Source): Result<Source>

    /**
     * Return a list of all [Source] of type [sourceType]. The result is not paginated.
     */
    fun findAll(sourceType: SourceType): Result<List<Source>>

    /**
     * Return a list of all [Source]. The result is not paginated.
     */
    fun findAll(): Result<List<Source>>
}
