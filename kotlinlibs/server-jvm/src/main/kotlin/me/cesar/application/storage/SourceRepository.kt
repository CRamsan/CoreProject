package me.cesar.application.storage

import org.springframework.data.repository.CrudRepository

/**
 * CRUD repository for [Source] entities.
 *
 * @author cramsan
 */
interface SourceRepository : CrudRepository<Source, Long>
