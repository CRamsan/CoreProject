package me.cesar.application.server.me.controller

import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.cesar.application.common.model.Article
import me.cesar.application.common.network.PageResponse
import me.cesar.application.server.me.article1
import me.cesar.application.server.me.article2
import me.cesar.application.spring.createObjectMapper
import me.cesar.application.spring.service.ArticleService
import me.cesar.application.spring.service.SourceService
import me.cesar.application.spring.service.ingestion.IngestorService
import me.cesar.application.spring.storage.mongodb.MongoDBArticleRepository
import me.cesar.application.spring.storage.mongodb.MongoDBSourceRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals

@WebMvcTest
internal class ArticleAPIControllerTest {

    @MockkBean
    lateinit var service: ArticleService

    @MockkBean
    lateinit var ingestorService: IngestorService

    @MockkBean
    lateinit var sourceService: SourceService

    @MockkBean
    lateinit var mongoDBArticleRepository: MongoDBArticleRepository

    @MockkBean
    lateinit var mongoDBSourceRepository: MongoDBSourceRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = createObjectMapper()

    @BeforeEach
    fun setUp() = Unit

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `should return all accounts`() {
        // WHEN
        every { service.findAll(any()) } returns Result.success(PageImpl(listOf(article1, article2)))

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/article")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: PageResponse<Article> = mapper.readValue(result.response.contentAsString)
        assertEquals(2, body.size)
    }

    @Test
    fun `should return single account by id`() {
        // WHEN
        every { service.findArticle("abc") } returns Result.success(article1)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/article/abc")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: Article = mapper.readValue(result.response.contentAsString)
        assertEquals(article1, body)
    }
}