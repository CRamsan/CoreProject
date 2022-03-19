package me.cesar.application.source

import me.cesar.application.source.rss.RssFetcher
import me.cesar.application.storage.Article
import me.cesar.application.storage.ArticleRepository
import me.cesar.application.storage.Source
import me.cesar.application.storage.SourceRepository
import me.cesar.application.storage.SourceType
import org.springframework.stereotype.Component

/**
 * The [Ingestor] will load determine all the sources and fetch any new [Article] and persist them in the
 * [ArticleRepository].
 *
 * @author cramsan
 */
@Component
class Ingestor(
    private val sourceRepository: SourceRepository,
    private val articleRepository: ArticleRepository,
) {
    /**
     * Initiate the ingestion process. All [Source] in the [SourceRepository] will be processes sequentially.
     * Based on their [SourceType], the respective implementation of [BaseFetcher] will be called. Any new articles
     * fetched will be persisted in the [ArticleRepository].
     */
    fun processAllSources() {
        sourceRepository.findAll().forEach {
            when (it.sourceType) {
                SourceType.RSS -> processRssSource(it)
            }
        }
    }

    private fun processRssSource(it: Source) {
        val fetcher = RssFetcher(it, sourceRepository)
        val newArticles = fetcher.processArticles()
        articleRepository.saveAll(newArticles)
    }
}
