package me.cesar.application.api

import me.cesar.application.Constants
import me.cesar.application.model.Article
import me.cesar.application.service.ArticleService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Expose access to the [Article] instances through a REST API.
 *
 * @author cramsan
 */
@RestController
@RequestMapping(Constants.ARTICLE_API)
class ArticleAPIController(
    private val service: ArticleService,
) {

    /**
     * Get a paginated list of [Article]. The caller can provide a [page] number to fetch and a [count]of elements to
     * retrieve. The entities will be sorted with [sortBy] before they are paginated.
     * If no arguments are provided, default values will be used.
     */
    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") count: Int,
        @RequestParam(defaultValue = "publishedAt") sortBy: String,
    ): List<Article> {
        return service.findAllByOrderByPublishedAtDesc(page, count, sortBy).content
    }

    /**
     * Get a single [Article] identified with [id].
     */
    @GetMapping("{id}")
    fun findOne(@PathVariable id: String) =
        service.findById(id.toLongOrNull() ?: 0) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article does not exist")
}
