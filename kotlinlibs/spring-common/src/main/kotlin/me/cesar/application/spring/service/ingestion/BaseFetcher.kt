package me.cesar.application.spring.service.ingestion

import kotlinx.datetime.Clock
import me.cesar.application.common.model.Article
import me.cesar.application.common.model.Source
import me.cesar.application.spring.service.ArticleService
import me.cesar.application.spring.service.SourceService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for other [Article] fetchers. This class will manage the last update time of the [Source].
 *
 * @author cramsan
 */
abstract class BaseFetcher(
    protected val source: Source,
    protected val sourceService: SourceService,
    protected val articleService: ArticleService,
    protected val clock: Clock,
) {

    private var logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Fetch a new list of [Article]. The new [Article]s will be returned as a [List].
     */
    fun processArticles(): List<Article> {
        logger.info("Starting to process articles for $source")

        val id = source.id
        val sourceEntity = sourceService.findSource(id).getOrThrow()
        check(sourceEntity != null) {
            "Source with id: $id not found"
        }
        val lastFetch = sourceEntity.lastUpdated
        val currentTime = clock.now()

        val newArticles = mutableListOf<Article>()
        val fetchedArticles = fetchArticles()
        logger.debug("Fetched ${fetchedArticles.size} articles from $source")

        fetchedArticles.forEach {
            if (it.publishedTime < lastFetch) {
                return@forEach
            }
            newArticles.add(it)
        }
        logger.debug("New ${newArticles.size} articles detected from $source")

        val updatedEntity = sourceEntity.copy(
            lastUpdated = currentTime,
        )
        articleService.insert(newArticles).getOrThrow()
        sourceService.save(updatedEntity).getOrThrow()

        logger.info("LastUpdate for $source was updated to $currentTime")

        return newArticles
    }

    protected abstract fun fetchArticles(): List<Article>
}
