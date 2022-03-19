package me.cesar.application.source

import me.cesar.application.storage.Article
import me.cesar.application.storage.Source
import me.cesar.application.storage.SourceRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Date

/**
 * Base class for other [Article] fetchers. This class will manage the last update time of the [Source].
 *
 * @author cramsan
 */
abstract class BaseFetcher(
    protected val source: Source,
    protected val sourceRepository: SourceRepository,
) {

    private var logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Return a [List] of [Article] that are considered as new. This will be determined based on the [Article] whose
     * [Article.publishedAt] is greater than [Source.lastUpdated].
     */
    fun processArticles(): List<Article> {
        logger.info("Starting to process articles for $source")

        val id = requireNotNull(source.id)
        val sourceEntity = sourceRepository.findById(id).get()
        val lastFetch = sourceEntity.lastUpdated
        val currentTime = Date()

        val newArticles = mutableListOf<Article>()
        val fetchedArticles = fetchArticles()
        logger.debug("Fetched ${fetchedArticles.size} articles from $source")

        fetchedArticles.forEach {
            if (it.publishedAt < lastFetch) {
                return@forEach
            }
            newArticles.add(it)
        }
        logger.debug("New ${newArticles.size} articles from detected from $source")

        sourceEntity.lastUpdated = currentTime
        sourceRepository.save(sourceEntity)

        logger.info("LastUpdate for $source was updated to $currentTime")

        return newArticles
    }

    protected abstract fun fetchArticles(): List<Article>
}
