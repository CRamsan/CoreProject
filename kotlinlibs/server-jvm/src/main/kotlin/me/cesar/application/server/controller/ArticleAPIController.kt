package me.cesar.application.server.controller

import me.cesar.application.common.NetworkPath
import me.cesar.application.common.model.Article
import me.cesar.application.spring.service.ArticleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Expose access to the [Article] instances through a REST API.
 *
 * @author cramsan
 */
@RestController
@RequestMapping(NetworkPath.ARTICLE_API)
class ArticleAPIController(
    private val service: ArticleService,
) {

    /**
     * Get a list of [Article].
     */
    @GetMapping
    fun findAll(): List<Article> {
        return service.findAll().getOrThrow()
    }

    /**
     * Get a single [Article] identified with [id].
     */
    @GetMapping("{id}")
    fun findOne(@PathVariable id: String) {
        service.findArticle(id).getOrThrow()
    }
}
