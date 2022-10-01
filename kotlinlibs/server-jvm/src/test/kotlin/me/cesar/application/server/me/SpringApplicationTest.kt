package me.cesar.application.server.me

import me.cesar.application.spring.service.ArticleService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull


/**
 * @author cramsan
 */
@SpringBootTest
class SpringApplicationTest {

    @Autowired
    lateinit var articleService: ArticleService


    @Test
    fun contextLoads() {
        assertNotNull(articleService)
    }

}