package me.cesar.application.service

import com.cramsan.framework.test.TestBase
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import kotlinx.datetime.Instant
import me.cesar.application.storage.Article
import me.cesar.application.storage.ArticleRepository
import org.junit.jupiter.api.Assertions.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.Date
import java.util.Optional
import kotlin.test.Test

/**
 * @author cramsan
 */
class ArticleServiceTest : TestBase() {

    lateinit var service: ArticleService

    @RelaxedMockK
    lateinit var repository: ArticleRepository

    override fun setupTest() {
        service = ArticleService(repository)
    }

    @Test
    fun `Testing finding by id`() {
        val articleId = 100L
        val articleEntity = Article(
            id = articleId,
            title = "test",
            source = "source",
            content = "content",
            publishedAt = Date(10000),
            addedAt = Date(20000),
        )
        every { repository.findById(articleId) } returns Optional.of(articleEntity)

        val article = service.findById(articleId)

        assertEquals(articleId, article?.id)
        assertEquals("test", article?.title)
        assertEquals("source", article?.source)
        assertEquals("content", article?.content)
        assertEquals(Instant.fromEpochSeconds(10000), article?.publishedAt)
    }

    @Test
    fun `Testing finding multiple`() {
        val slot = slot<PageRequest>()
        val mockPage: Page<Article> = PageImpl(listOf(mockk(relaxed = true), mockk(relaxed = true)))
        val page = 5
        val count = 100
        val sortBy = "byPublishedAtDesc"
        val expectedPagingRequest = PageRequest.of(page, count, Sort.by(sortBy))
        every { repository.findAllByOrderByPublishedAtDesc(capture(slot)) } returns mockPage

        val articles = service.findAllByOrderByPublishedAtDesc(
            page,
            count,
            sortBy,
        )

        assertEquals(2, articles.size)
        assertEquals(expectedPagingRequest, slot.captured)
    }
}