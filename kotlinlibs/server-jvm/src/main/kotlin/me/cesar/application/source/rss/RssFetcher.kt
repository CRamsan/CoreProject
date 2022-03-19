package me.cesar.application.source.rss

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import me.cesar.application.source.BaseFetcher
import me.cesar.application.storage.Article
import me.cesar.application.storage.Source
import me.cesar.application.storage.SourceRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

/**
 * Implementation of the [BaseFetcher] that retrieves [Article] from an RSS source.
 * @author cramsan
 */
class RssFetcher(
    source: Source,
    sourceRepository: SourceRepository,
) : BaseFetcher(
    source,
    sourceRepository,
) {
    private val input = SyndFeedInput()

    private var logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun fetchArticles(): List<Article> {
        logger.info("Starting to process RSS articles for $source")
        val url = URL(source.url)
        val feed: SyndFeed = input.build(XmlReader(url))

        logger.info("${feed.entries.size} RSS articles found for $source")

        return feed.entries.map {
            Article(
                title = it.title,
                content = it.contents.joinToString("\n"),
                publishedAt = it.publishedDate,
            )
        }
    }
}
