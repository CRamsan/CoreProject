package me.cesar.application.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kotlinx.datetime.Clock
import me.cesar.application.model.Article
import me.cesar.application.service.ArticleService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.BeforeTest

@WebMvcTest
class ArticleAPIControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var articleService: ArticleService

    @BeforeTest
    fun setup() { }

    @Test
    fun `List articles`() {
        val article1 = Article("Title 1", "blog1", "A", Clock.System.now(), 0)
        val article2 = Article("Title 2", "blog2", "B", Clock.System.now(), 1)
        val body = listOf(article1, article2)
        every { articleService.findAllByOrderByPublishedAtDesc(0, 10, "publishedAt") } returns PageImpl(body)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/article/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
    }

    @Test
    fun `Get article`() {
        val article = Article("Title 1", "blog1", "A", Clock.System.now(), 100)
        every { articleService.findById(100) } returns article

        mockMvc.perform(MockMvcRequestBuilders.get("/api/article/100").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(100L))
            .andReturn()
    }
}
