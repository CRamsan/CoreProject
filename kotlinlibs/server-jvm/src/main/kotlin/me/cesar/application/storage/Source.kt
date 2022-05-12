package me.cesar.application.storage

import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Entity class to persist an source in the database.
 * The source represents a location that can be accessed to retrieve new content.
 *
 * @author cramsan
 */
@Entity
class Source(
    var title: String,
    var url: String,
    var lastUpdated: Date,
    var sourceType: SourceType,
    @Id @GeneratedValue var id: Long? = null,
)
