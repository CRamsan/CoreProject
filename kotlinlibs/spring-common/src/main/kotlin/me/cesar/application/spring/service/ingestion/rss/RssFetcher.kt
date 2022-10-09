package me.cesar.application.spring.service.ingestion.rss

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinInstant
import me.cesar.application.common.model.Article
import me.cesar.application.common.model.Source
import me.cesar.application.spring.service.ArticleService
import me.cesar.application.spring.service.SourceService
import me.cesar.application.spring.service.ingestion.BaseFetcher
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

/**
 * Implementation of the [BaseFetcher] that retrieves [Article] from an RSS source.
 * @author cramsan
 */
class RssFetcher(
    source: Source,
    sourceService: SourceService,
    articleService: ArticleService,
    clock: Clock,
) : BaseFetcher(
    source,
    sourceService,
    articleService,
    clock,
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
                id = ObjectId().toHexString(),
                title = it.title,
                sourceId = source.id,
                content = it.contents.joinToString("\n"),
                bannerUrl = "https://blog.jetbrains.com/wp-content/uploads/2022/10/Blog_JavaScript-day_feat.png",
                publishedTime = it.publishedDate.toInstant().toKotlinInstant(),
            )
        }
    }
}
