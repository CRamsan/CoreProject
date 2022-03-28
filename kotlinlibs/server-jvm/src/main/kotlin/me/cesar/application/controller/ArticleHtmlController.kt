package me.cesar.application.controller

import me.cesar.application.BlogProperties
import me.cesar.application.service.ArticleService
import me.cesar.application.storage.Article
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
class ArticleHtmlController(
    private val articleService: ArticleService,
    private val properties: BlogProperties,
) {

    @GetMapping("/")
    fun blog(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") count: Int,
        @RequestParam(defaultValue = "publishedAt") sortBy: String,
        model: Model,
    ): String {
        model["title"] = properties.title
        model["banner"] = properties.banner
        model["articles"] = articleService.findAllByOrderByPublishedAtDesc(
            page, count, sortBy
        ).map { it.render() }
        return "blog"
    }

    @GetMapping("/article/{id}")
    fun article(@PathVariable id: String, model: Model): String {
        val article = articleService
            .findById(id.toLongOrNull() ?: 0)
            ?.render()
            ?: throw ResponseStatusException(NOT_FOUND, "This article does not exist")
        model["title"] = article.title
        model["article"] = article
        return "article"
    }

    fun Article.render() = RenderedArticle(
        id.toString(),
        title,
        content,
        addedAt.toString(),
    )

    data class RenderedArticle(
        val id: String,
        val title: String,
        val content: String,
        val addedAt: String
    )
}
