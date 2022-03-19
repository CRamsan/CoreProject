package me.cesar.application.source.rss

import com.cramsan.framework.test.TestBase
import io.mockk.every
import io.mockk.mockk
import me.cesar.application.storage.Source
import me.cesar.application.storage.SourceRepository
import me.cesar.application.storage.SourceType
import java.io.File
import java.net.URL
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author cramsan
 */
class RSSIngestorTest : TestBase() {

    lateinit var ingestor: RssFetcher

    lateinit var sourceRepository: SourceRepository

    private val testUrl: URL

    init {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(RSS_FILE).file)
        testUrl = file.toURI().toURL()
    }

    override fun setupTest() {
        val oldDate = Date.from(Instant.ofEpochMilli(0))
        val source = Source(
            "",
            testUrl.toString(),
            oldDate,
            SourceType.RSS,
            0,
        )

        sourceRepository = mockk()

        every { sourceRepository.findById(any()) } returns Optional.of(
            Source(
                "",
                testUrl.toString(),
                oldDate,
                SourceType.RSS,
                0,
            )
        )

        every { sourceRepository.save(any()) } returns mockk()

        ingestor = RssFetcher(
            source,
            sourceRepository,
        )
    }

    @Test
    fun testLoadEntries() {
        val articles = ingestor.processArticles()

        assertEquals(12, articles.size)
    }

    @Test
    fun testLoadRecentEntries() {
        // Fri, 04 Mar 2022 14:48:20 +0000
        val date = LocalDate.of(2022, Month.MARCH, 9)
        val zonedDateTime: ZonedDateTime = date.atStartOfDay(ZoneOffset.UTC)
        val utilDate = Date.from(zonedDateTime.toInstant())

        every { sourceRepository.findById(any()) } returns Optional.of(
            Source(
                "",
                testUrl.toString(),
                utilDate,
                SourceType.RSS,
                0,
            )
        )

        val articles = ingestor.processArticles()

        assertEquals(4, articles.size)
    }

    companion object {
        const val RSS_FILE = "kotlin-blog.rss"
    }
}
