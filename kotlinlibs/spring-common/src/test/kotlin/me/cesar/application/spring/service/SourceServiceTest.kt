package me.cesar.application.spring.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Instant
import me.cesar.application.common.model.SourceType
import me.cesar.application.spring.source1
import me.cesar.application.spring.source2
import me.cesar.application.spring.storage.SourceRepository
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

internal class SourceServiceTest {

    @MockK
    lateinit var repository: SourceRepository

    lateinit var service: SourceService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = SourceService(repository)
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `findSource returns a single source`() {
        // WHEN
        every { repository.findSource("id1") } returns Result.success(source1)

        // DO
        val result = service.findSource("id1").getOrThrow()

        // ASSERT
        assertEquals(source1, result)
    }

    @Test
    fun `findArticle return null when article not found`() {
        // WHEN
        every { repository.findSource("id1") } returns Result.success(null)

        // DO
        val result = service.findSource("id1").getOrThrow()

        // ASSERT
        assertNull(result)
    }

    @Test
    fun `insert should save new `() {
        // WHEN
        every { repository.insert(source1) } returns Result.success(source1)

        // DO
        service.insert(source1).getOrThrow()

        // ASSERT
        verify {
            repository.insert(source1)
        }
    }

    @Test
    fun `findAll returns a list of all articles`() {
        // WHEN
        val pageable: Pageable = mockk()
        val resultPage = PageImpl(listOf(
            source1, source2,
        ))
        every { repository.findAll(pageable) } returns Result.success(resultPage)

        // DO
        val result = service.findAll(pageable).getOrThrow()

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
            source2,
        ))
        every { repository.findAll(SourceType.RSS, pageable) } returns Result.success(resultPage)

        // DO
        val result = service.findAll(SourceType.RSS, pageable).getOrThrow()

        // ASSERT
        verify {
            repository.findAll(SourceType.RSS, pageable)
        }
        assertEquals(listOf(source2), result.content)
    }

    @Test
    fun `save should update an `() {
        val updatedSource = source1.copy(
            title = "updated title",
            url = "https://abc",
            lastUpdated = Instant.fromEpochSeconds(900),
        )

        // WHEN
        every { repository.save(updatedSource) } returns Result.success(Unit)

        // DO
        service.save(updatedSource).getOrThrow()

        // ASSERT
        verify {
            repository.save(updatedSource)
        }
    }
}