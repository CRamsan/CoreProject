package me.cesar.application.spring.storage.mongodb

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Instant
import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.source1
import me.cesar.application.spring.source1Entity
import me.cesar.application.spring.source2
import me.cesar.application.spring.source2Entity
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

internal class MongoDBSourceRepositoryProxyTest {

    @MockK
    lateinit var repository: MongoDBSourceRepository

    lateinit var proxy: MongoDBSourceRepositoryProxy

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        proxy = MongoDBSourceRepositoryProxy(repository)
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `findSource returns a single source`() {
        // WHEN
        every { repository.findById("id1") } returns Optional.of(source1Entity)

        // DO
        val result = proxy.findSource("id1").getOrThrow()

        // ASSERT
        assertEquals(source1, result)
    }

    @Test
    fun `findArticle return null when article not found`() {
        // WHEN
        every { repository.findById("id1") } returns Optional.empty()

        // DO
        val result = proxy.findSource("id1").getOrThrow()

        // ASSERT
        assertNull(result)
    }

    @Test
    fun `insert should save new entity`() {
        // WHEN
        every { repository.insert(source1Entity) } returns source1Entity

        // DO
        proxy.insert(source1).getOrThrow()

        // ASSERT
        verify {
            repository.insert(source1Entity)
        }
    }

    @Test
    fun `findAll returns a list of all articles`() {
        // WHEN
        val pageable: Pageable = mockk()
        val resultPage = PageImpl(listOf(
            source1Entity, source2Entity,
        ))
        every { repository.findAll(pageable) } returns resultPage

        // DO
        val result = proxy.findAll(pageable).getOrThrow()

        // ASSERT
        verify {
            repository.findAll(pageable)
        }
        assertEquals(listOf(source1, source2), result.content)
    }

    @Test
    fun `findAll for a sourceId returns a list of articles that match the sourceId`() {
        // WHEN
        val pageable: Pageable = mockk()
        val resultPage = PageImpl(listOf(
            source2Entity,
        ))
        every { repository.findBySourceType(SourceType.RSS, pageable) } returns resultPage

        // DO
        val result = proxy.findAll(SourceType.RSS, pageable).getOrThrow()

        // ASSERT
        verify {
            repository.findBySourceType(SourceType.RSS, pageable)
        }
        assertEquals(listOf(source2), result.content)
    }

    @Test
    fun `save should update an entity`() {
        val updatedEntity = source1Entity.copy(
            title = "updated title",
            url = "https://abc",
            lastUpdated = 900,
        )

        val updatedSource = source1.copy(
            title = "updated title",
            url = "https://abc",
            lastUpdated = Instant.fromEpochSeconds(900),
        )

        // WHEN
        every { repository.save(updatedEntity) } returns updatedEntity

        // DO
        proxy.save(updatedSource).getOrThrow()

        // ASSERT
        verify {
            repository.save(updatedEntity)
        }
    }
}