package me.cesar.application.storage

import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob

/**
 * Entity for storing the information about a single article.
 *
 * @author cramsan
 */
@Entity
class Article(
    var title: String,
    var source: String,
    @Lob
    var content: String,
    var publishedAt: Date,
    var addedAt: Date = Date(),
    @Id @GeneratedValue var id: Long? = null,
)
