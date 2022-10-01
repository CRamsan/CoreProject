package me.cesar.application.spring.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Instant
import me.cesar.application.spring.article1
import me.cesar.application.spring.article2
import me.cesar.application.spring.article3
import me.cesar.application.spring.storage.ArticleRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import kotlin.test.assertNull

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 7/7/2022 17:18
 */

internal class ArticleServiceTest {

    @MockK
    lateinit var repository: ArticleRepository

    lateinit var service: ArticleService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = ArticleService(repository)
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `findArticle returns a single article`() {
        // WHEN
        every { repository.findArticle("id1") } returns Result.success(article1)

        // DO
        val result = service.findArticle("id1").getOrThrow()

        // ASSERT
        assertEquals(article1, result)
    }

    @Test
    fun `findArticle return null when article not found`() {
        // WHEN
        every { repository.findArticle("id1") } returns Result.success(null)

        // DO
        val result = service.findArticle("id1").getOrThrow()

        // ASSERT
        assertNull(result)
    }

    @Test
    fun `insert should save new `() {
        // WHEN
        every { repository.insert(article1) } returns Result.success(Unit)

        // DO
        service.insert(article1).getOrThrow()

        // ASSERT
        verify {
            repository.insert(article1)
        }
    }

    @Test
    fun `insert should save list of new entities`() {
        // WHEN
        every { repository.insert(listOf(article1, article2, article3)) } returns Result.success(Unit)

        // DO
        service.insert(listOf(article1, article2, article3)).getOrThrow()

        // ASSERT
        verify {
            repository.insert(listOf(article1, article2, article3))
        }
    }

    @Test
    fun `findAll returns a list of all articles`() {
        // WHEN
        val pageable: Pageable = mockk()
        val resultPage = PageImpl(listOf(
            article1, article2,
        ))
        every { repository.findAll(pageable) } returns Result.success(resultPage)

        // DO
        val result = service.findAll(pageable).getOrThrow()

        // ASSERT
        verify {
            repository.findAll(pageable)
        }
        assertEquals(listOf(article1, article2), result.content)
    }

    @Test
    fun `findAll for a sourceId returns a list of articles that match the sourceId`() {
        // WHEN
        val pageable: Pageable = mockk()
        val resultPage = PageImpl(listOf(
            article2, article3,
        ))
        every { repository.findAll("sourceId", pageable) } returns Result.success(resultPage)

        // DO
        val result = service.findAll("sourceId", pageable).getOrThrow()

        // ASSERT
        verify {
            repository.findAll("sourceId", pageable)
        }
        assertEquals(listOf(article2, article3), result.content)
    }

    @Test
    fun `save should update an `() {
        val updatedArticle = article1.copy(
            title = "updated title",
            content = "new content",
            sourceId = "abc",
            publishedTime = Instant.fromEpochSeconds(900),
        )

        // WHEN
        every { repository.save(updatedArticle) } returns Result.success(Unit)

        // DO
        service.save(updatedArticle).getOrThrow()

        // ASSERT
        verify {
            repository.save(updatedArticle)
        }
    }
}