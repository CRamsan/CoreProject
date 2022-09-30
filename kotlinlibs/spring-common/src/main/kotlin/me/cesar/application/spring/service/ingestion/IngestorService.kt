package me.cesar.application.spring.service.ingestion

import kotlinx.datetime.Clock
import me.cesar.application.common.model.Source
import me.cesar.application.common.model.SourceType
import me.cesar.application.common.network.IngestionResult
import me.cesar.application.common.network.SourceIngestionResult
import me.cesar.application.spring.service.ArticleService
import me.cesar.application.spring.service.SourceService
import me.cesar.application.spring.service.ingestion.rss.RssFetcher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

/**
 * The [IngestorService] will load determine all the sources and fetch any new [Article] and persist them.
 *
 * @author cramsan
 */
@Component
class IngestorService(
    private val sourceService: SourceService,
    private val articleService: ArticleService,
    private val clock: Clock,
) {
    /**
     * Initiate the ingestion process. All [Source] in the [SourceService] will be processes sequentially.
     * Based on their [SourceType], the respective implementation of [BaseFetcher] will be called. Any new articles
     * fetched will be persisted in the [ArticleService].
     */
    fun processAllSources(): Result<IngestionResult> = runCatching {
        var page: Pageable? = null
        val newIngestions = mutableListOf<SourceIngestionResult>()
        do {
            val sources = sourceService.findAll(page).getOrThrow()
            val newIngestionForPage = sources.mapNotNull { source ->
                val newArticleCount = when (source.sourceType) {
                    SourceType.RSS -> processRssSource(source)
                    SourceType.UNKNOWN -> return@mapNotNull null
                }
                SourceIngestionResult(
                    source = source,
                    newArticleCount = newArticleCount,
                )
            }
            page = if (sources.hasNext()) {
                sources.nextPageable()
            } else {
                null
            }
            newIngestions += newIngestionForPage
        } while (page != null)
        IngestionResult(newIngestions)
    }

    /**
     * Initiate the ingestion process for a single source identified by the [sourceId] and [sourceType].
     */
    fun processSingleSources(source: Source): Result<IngestionResult> = runCatching {
        val newArticleCount = when (source.sourceType) {
            SourceType.RSS -> processRssSource(source)
            SourceType.UNKNOWN -> 0
        }
        val newIngestion = SourceIngestionResult(
            source = source,
            newArticleCount = newArticleCount,
        )

        IngestionResult(listOf(newIngestion))
    }

    private fun processRssSource(source: Source): Int {
        val fetcher = RssFetcher(
            source,
            sourceService,
            articleService,
            clock,
        )
        val newArticles = fetcher.processArticles()
        return newArticles.size
    }
}
