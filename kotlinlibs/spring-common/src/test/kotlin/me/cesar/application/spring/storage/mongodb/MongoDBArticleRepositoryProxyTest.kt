package me.cesar.application.spring.storage.mongodb

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Instant
import me.cesar.application.spring.article1
import me.cesar.application.spring.article2
import me.cesar.application.spring.article3
import me.cesar.application.spring.articleEntity1
import me.cesar.application.spring.articleEntity2
import me.cesar.application.spring.articleEntity3
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.test.assertNull

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 7/7/2022 17:18
 */

internal class MongoDBArticleRepositoryProxyTest {

    @MockK
    lateinit var repository: MongoDBArticleRepository

    lateinit var proxy: MongoDBArticleRepositoryProxy

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        proxy = MongoDBArticleRepositoryProxy(repository)
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `findArticle returns a single article`() {
        // WHEN
        every { repository.findById("id1") } returns Optional.of(articleEntity1)

        // DO
        val result = proxy.findArticle("id1").getOrThrow()

        // ASSERT
        assertEquals(article1, result)
    }

    @Test
    fun `findArticle return null when article not found`() {
        // WHEN
        every { repository.findById("id1") } returns Optional.empty()

        // DO
        val result = proxy.findArticle("id1").getOrThrow()

        // ASSERT
        assertNull(result)
    }

    @Test
    fun `insert should save new entity`() {
        // WHEN
        every { repository.insert(articleEntity1) } returns articleEntity1

        // DO
        proxy.insert(article1).getOrThrow()

        // ASSERT
        verify {
            repository.insert(articleEntity1)
        }
    }

    @Test
    fun `insert should save list of new entities`() {
        // WHEN
        every { repository.insert(listOf(articleEntity1, articleEntity2, articleEntity3)) } returns listOf()

        // DO
        proxy.insert(listOf(article1, article2, article3)).getOrThrow()

        // ASSERT
        verify {
            repository.insert(listOf(articleEntity1, articleEntity2, articleEntity3))
        }
    }

    @Test
    fun `findAll returns a list of all articles`() {
        // WHEN
        val pageable: Pageable = mockk()
        val resultPage = PageImpl(listOf(
            articleEntity1, articleEntity2,
        ))
        every { repository.findAll(pageable) } returns resultPage

        // DO
        val result = proxy.findAll(pageable).getOrThrow()

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
            articleEntity2, articleEntity3,
        ))
        every { repository.findBySourceId("sourceId", pageable) } returns resultPage

        // DO
        val result = proxy.findAll("sourceId", pageable).getOrThrow()

        // ASSERT
        verify {
            repository.findBySourceId("sourceId", pageable)
        }
        assertEquals(listOf(article2, article3), result.content)
    }

    @Test
    fun `save should update an entity`() {
        val updatedEntity = articleEntity1.copy(
            title = "updated title",
            content = "new content",
            sourceId = "abc",
            lastUpdated = 900,
        )

        val updatedArticle = article1.copy(
            title = "updated title",
            content = "new content",
            sourceId = "abc",
            publishedTime = Instant.fromEpochSeconds(900),
        )

        // WHEN
        every { repository.save(updatedEntity) } returns updatedEntity

        // DO
        proxy.save(updatedArticle).getOrThrow()

        // ASSERT
        verify {
            repository.save(updatedEntity)
        }
    }
}